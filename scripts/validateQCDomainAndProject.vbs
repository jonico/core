' validateQCDomainAndProject.vbs
' Checks whether user has access to passed QC domain and project
' arg 0: QC URL
' arg 1: QC user
' arg 2: QC password
' arg 3: QC domain
' arg 4: QC project
'
' output: success element if domain, project and requirement type exist, error element in all other cases
'
' call it like
' cscript //B //U //T:20 validateQCDomainAndProject.vbs http://<qchost>:8080/qcbin/ <user> <password> <domain> <project>

Public Sub ErrorMessage(Message, XmlDocument)
	XmlDocument.documentElement=XmlDocument.createElement("error")
	XmlDocument.documentElement.text = Message
	Output (XmlDocument.xml)
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
Dim QcProject
Dim Projects
Set args = WScript.Arguments
QcURL = args.Item(0)
QcUser = args.Item(1)
QcPassword = args.Item(2)
QcDomain = args.Item(3)
QcProject = args.Item(4)

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
				tdConnection.Connect QCDomain, QCProject
					If tdConnection.ProjectConnected = False Then
					ErrorMessage "Invalid domain/project", XmlDocument
					WScript.Sleep 1000
				Else
					XmlDocument.documentElement=XmlDocument.createElement("success")
					XmlDocument.documentElement.text = "Valid domain/project"
					Output (XmlDocument.xml)
					WScript.Sleep 1000
					tdConnection.DisconnectProject
				End If
				tdConnection.Logout
			End If
			tdConnection.ReleaseConnection
		End If
	End If	
End If