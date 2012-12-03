' validateQCDomainAndProjectAndRequirementType.vbs
' Checks whether user has access to passed QC domain, project and requirement type
' arg 0: QC URL
' arg 1: QC user
' arg 2: QC password
' arg 3: QC domain
' arg 4: QC project
' arg 5: QC Requirement Type
' 
' output: success element if domain, project and requirement type exist, error element in all other cases
'
' call it like
' cscript //B //U //T:20 validateQCDomainAndProjectAndRequirementType.vbs http://<qchost>:8080/qcbin/ <user> <password> <domain> <project> <requirement type> 

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
Dim RequirementType
Dim Projects
Dim cust
Dim custFields
Dim aCustField
Dim custLists
Dim aCustList
Dim foundReqType

' Write XML header
Output ("<?xml version=""1.0"" encoding=""UTF-8"" standalone=""yes""?>" & vbCrLf)

Set args = WScript.Arguments
If args.Count > 6 Then
	Output ("<error>Wrong number of arguments supplied</error>")
	WScript.Quit 1
End If 

QcURL = args.Item(0)
QcUser = args.Item(1)
QcPassword = args.Item(2)
QcDomain = args.Item(3)
QcProject = args.Item(4)
RequirementType = args.Item(5)


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
					ErrorMessage "Invalid domain/project/requirement type", XmlDocument
				Else
					Set Cust = tdConnection.Customization
    				Set CustTypes = Cust.Types
					Set ReqTypes = CustTypes.GetEntityCustomizationTypes(0)
					foundReqType = false
    				For Each ReqType In ReqTypes
    					If ReqType.Name = RequirementType Then
    						foundReqType = true
    						XmlDocument.documentElement=XmlDocument.createElement("success")
		    				XmlDocument.documentElement.text = "Valid domain/project/requirement type"
		    				Output (XmlDocument.xml)
    					End If
    				Next
    				If Not foundReqType Then
    					ErrorMessage "Invalid domain/project/requirement type", XmlDocument
    				End If
    				WScript.Sleep 1000
					tdConnection.DisconnectProject
				End If
				tdConnection.Logout
			End If
			tdConnection.ReleaseConnection
		End If
	End If
End If