' getQCRequirementFields.vbs
' returns all QC fields of a defect
' arg 0: QC URL
' arg 1: QC user
' arg 2: QC password
' arg 3: QC domain
' arg 4: QC project
' arg 5: QC requirement type
' output: <field> elements with all characteristics as attributes and child <value> elements for available field values enclosed in <success> root element on success
'		  error root element with enclosed error message on failure
'		  Releases will be transported in <release> elements with <cycle> sub elements 
' call it like
' cscript //B  //T:20 getQCRequirementFields.vbs http://<qchost>:8080/qcbin/ <user> <password> <domain> <project> <requirement type>

Public Sub ErrorMessage(Message, XmlDocument)
	XmlDocument.documentElement=XmlDocument.createElement("error")
	XmlDocument.documentElement.text = Message
	Output (XmlDocument.xml)
End Sub

Public Sub Output (Content)
	WScript.StdOut.WriteLine Content
End Sub

Public Sub AddAttribute (xmlDoc, xmlElement, attributeName, attributeValue)
	Set attribute = xmlDoc.createAttribute(attributeName)
	attribute.text = attributeValue
	xmlElement.attributes.setNamedItem(attribute)
End Sub

Public Sub AddFieldValues (xmlDocument, fieldElement, nodes)
	For Each node in nodes
		If node.ChildrenCount > 0 Then
			AddFieldValues xmlDocument, fieldElement, node.Children
		Else
			Set valueElement = xmlDocument.createElement("value")
			valueElement.text = node.Name
			fieldElement.appendChild (valueElement)	
		End If 
	Next
End Sub

Public Sub AddReleaseAndCycleFieldValues (xmlDocument, rootElement, node)
Set releaseFactoryFilter = node.ReleaseFactory.Filter
	'releaseFactoryFilter.Filter("REL_NAME") = "*" 
	Set listOfReleases = releaseFactoryFilter.NewList
	For Each release in listOfReleases
		' add a release element
		Set releaseElement = xmlDocument.createElement("release")
		AddAttribute xmlDocument, releaseElement, "Name", release.Name
		AddAttribute xmlDocument, releaseElement, "ID", release.ID
		rootElement.appendChild(releaseElement)
		Set cycleFactoryFilter = release.CycleFactory.Filter
		Set listOfCycles = cycleFactoryFilter.NewList
		For Each cycle In listOfCycles
			Set cycleElement = xmlDocument.createElement("cycle")
			AddAttribute xmlDocument, cycleElement, "Name", cycle.Name
			AddAttribute xmlDocument, cycleElement, "ID", cycle.ID 
			releaseElement.appendChild (cycleElement)
		Next	
	Next
	Set releaseFolderFactoryFilter = node.ReleaseFolderFactory.Filter
	Set listOfReleaseFolders = releaseFolderFactoryFilter.NewList
	For Each releaseFolder In listOfReleaseFolders
		AddReleaseAndCycleFieldValues xmlDocument, rootElement, releaseFolder
	Next
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
					ErrorMessage "Invalid domain/project", XmlDocument
				Else
					Set Cust = tdConnection.Customization
    				Set CustTypes = Cust.Types
					Set ReqTypes = CustTypes.GetEntityCustomizationTypes(0)
					foundReqType = false
    				For Each ReqType In ReqTypes
    					If ReqType.Name = RequirementType Then
    						foundReqType = true
    						XmlDocument.documentElement=XmlDocument.createElement("success")
		    				Set custFields = ReqType.Fields
		    				For Each custField In custFields
		    					Set aCustField = custField.Field
		    					If aCustField.IsActive Then
			        				Set FieldElement=XmlDocument.createElement("field")
			        				AddAttribute XmlDocument, FieldElement, "UserLabel", aCustField.UserLabel
			        				AddAttribute XmlDocument, FieldElement, "IsRequired", aCustField.IsRequired
			        				AddAttribute XmlDocument, FieldElement, "IsMultiValue", aCustField.IsMultiValue
			        				AddAttribute XmlDocument, FieldElement, "UserColumnType", aCustField.UserColumnType
			        				AddAttribute XmlDocument, FieldElement, "Type", aCustField.Type
			        				AddAttribute XmlDocument, FieldElement, "ColumnName", aCustField.ColumnName
			        				AddAttribute XmlDocument, FieldElement, "EditStyle", aCustField.EditStyle
			        				AddAttribute XmlDocument, FieldElement, "ColumnType", aCustField.ColumnType
			        				AddAttribute XmlDocument, FieldElement, "TableName", aCustField.TableName
			        				AddAttribute XmlDocument, FieldElement, "IsHistory", aCustField.IsHistory
			        				AddAttribute XmlDocument, FieldElement, "SupportsHistory", aCustField.IsSupportsHistory
			        				ListName = ""
		        					'If the field is linked to a custom list, get the name
		        					' of the list and the field properties.
		            				If Not (aCustField.List Is Nothing) Then
		            					AddFieldValues XmlDocument, FieldElement, aCustField.List.RootNode.Children
		            				End If
		         	        		XmlDocument.documentElement.appendChild(FieldElement)
			        			End If
		    				Next
		    				' Add release and cycle info
		    				AddReleaseAndCycleFieldValues XmlDocument, XmlDocument.documentElement, tdConnection.ReleaseFolderFactory.Root
		    				Output (XmlDocument.xml)
    					End If
    				Next
    				If Not foundReqType Then
    					ErrorMessage "Could not find specified requirement type", XmlDocument
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