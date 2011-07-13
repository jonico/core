' getQCDomains.vbs
' retrieves QC projects belonging to a domain
' arg 0: QC URL
' arg 1: QC user
' arg 2: QC password
' arg 3: QC domain
' output: QC projects visible to the user in <project> elements enclosed in <success> root element on success
'		  error root element with enclosed error message on failure
' call it like
' cscript //B //U //T:20 getQCProjects.vbs http://<qchost>:8080/qcbin/ <user> <password> <domain>

Public Sub ErrorMessage(Message, XmlDocument)
	XmlDocument.documentElement=XmlDocument.createElement("error")
	XmlDocument.documentElement.text = Message
End Sub

Public Sub Output (Content)
	WScript.StdOut.WriteLine Content
End Sub

' --- Main ---
On Error Resume Next
Dim args
Dim tdConnection
Dim QcURL
Dim QcUser
Dim QcPassword
Dim QcDomain
Dim Projects
Set args = WScript.Arguments
QcURL = args.Item(0)
QcUser = args.Item(1)
QcPassword = args.Item(2)
QcDomain = args.Item(3)

' Write XML header
Output ("<?xml version=""1.0"" encoding=""UTF-8"" standalone=""yes""?>" & vbCrLf)

Set XmlDocument = CreateObject("MSXML.DomDocument")
If XmlDocument Is Nothing Then
	Output ("<error>Could not create MSXML object</error>")
Else
	Set tdConnection = CreateObject("TDApiOle80.TDConnection")
	If tdConnection Is Nothing Then
			ErrorMessage "Could not create TDConnection object", XmlDocument
	Else
		tdConnection.InitConnectionEx QcURL
		If tdConnection.Connected = False Then
			ErrorMessage "Could not initialize QC connection", XmlDocument
		Else
			tdConnection.Login QCUser, QcPassword
			If tdConnection.LoggedIn = False Then
				ErrorMessage "Could not log into QC", XmlDocument
			Else
				Set Projects = tdConnection.VisibleProjects(QcDomain)
				If Projects Is Nothing Then
					ErrorMessage "Could not retrieve visible projects", XmlDocument
				Else
					XmlDocument.documentElement=XmlDocument.createElement("success")
					For Each Project in Projects
						Set ProjectElement = XmlDocument.createElement("project")
						ProjectElement.text = Project
						XmlDocument.documentElement.appendChild (ProjectElement)
					Next
				End If
				tdConnection.Logout
			End If
			tdConnection.ReleaseConnection
		End If
	End If	
	Output (XmlDocument.xml)
End If