' validateQCDomainAndProject.vbs
' validates that QC project and domain are valid
' arg 0: QC URL
' arg 1: QC user
' arg 2: QC password
' arg 3: QC domain
' arg 4: QC project
' output: requirement types in <type> elements enclosed in <success> root element on success
'		  error root element with enclosed error message on failure 
' call it like
' cscript //B //U //T:20 getQCRequirementTypes.vbs http://<qchost>:8080/qcbin/ <user> <password> <domain> <project>

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
Dim QcProject
Dim Projects
Dim ListName
Dim ReqType

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
				Else
					XmlDocument.documentElement=XmlDocument.createElement("success")
    				Set Cust = tdConnection.Customization
    				Set CustTypes = Cust.Types
    				' 0 is a magic constant for requirement types
    				Set ReqTypes = CustTypes.GetEntityCustomizationTypes(0)
    				For Each ReqType In ReqTypes
    					Set TypeElement = XmlDocument.createElement("type")
						TypeElement.text = reqType.Name
						XmlDocument.documentElement.appendChild (TypeElement)
    				Next
					tdConnection.DisconnectProject
				End If
				tdConnection.Logout
			End If
			tdConnection.ReleaseConnection
		End If
	End If	
	Output (XmlDocument.xml)
End If