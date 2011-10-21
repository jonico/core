' getQCDomains.vbs
' retrieves QC domains visible to the user
' arg 0: QC URL
' arg 1: QC user
' arg 2: QC password
' output: QC domains visible to the user in <domain> elements enclosed in <success> root element on success
'		  error root element with enclosed error message on failure
' call it like
' cscript //B //U //T:20 getQCDomains.vbs http://<qchost>:8080/qcbin/ <user> <password>

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
Dim Domains
Set args = WScript.Arguments
QcURL = args.Item(0)
QcUser = args.Item(1)
QcPassword = args.Item(2)

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
				Set Domains = tdConnection.VisibleDomains
				If Domains Is Nothing Then
					ErrorMessage "Could not retrieve visible domains", XmlDocument
				Else
					XmlDocument.documentElement=XmlDocument.createElement("success")
					For Each Domain in Domains
						Set DomainElement = XmlDocument.createElement("domain")
						DomainElement.text = Domain
						XmlDocument.documentElement.appendChild (DomainElement)
					Next
					Output (XmlDocument.xml)
				End If
				WScript.Sleep 1000
				tdConnection.Logout
			End If
			tdConnection.ReleaseConnection
		End If
	End If	
End If