<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://sakaiproject.org/jsf2/sakai" prefix="sakai" %>
<%@ taglib uri="http://sakaiproject.org/jsf/messageforums" prefix="mf" %>
<jsp:useBean id="msgs" class="org.sakaiproject.util.ResourceLoader" scope="session">
   <jsp:setProperty name="msgs" property="baseName" value="org.sakaiproject.api.app.messagecenter.bundle.Messages"/>
</jsp:useBean>

<f:view>
	<sakai:view title="#{msgs.pvt_delcon}">
<!--Y:\msgcntr\messageforums-app\src\webapp\jsp\privateMsg\pvtMsgDelete.jsp-->
    <h:form id="pvtMsgDelete">
           		<script>includeLatestJQuery("msgcntr");</script>
       		<script src="/messageforums-tool/js/sak-10625.js"></script>
       		<script src="/messageforums-tool/js/messages.js"></script>
    <div class="breadCrumb specialLink">
    	<h3>
	 	    <h:commandLink action="#{PrivateMessagesTool.processActionHome}" value="#{msgs.cdfm_message_forums}" title=" #{msgs.cdfm_message_forums}"/> /
  	    <h:commandLink action="#{PrivateMessagesTool.processActionPrivateMessages}" value="#{msgs.cdfm_message_pvtarea}" title=" #{msgs.cdfm_message_pvtarea}"/> /
		  <h:commandLink action="#{PrivateMessagesTool.processDisplayForum}" value="#{PrivateMessagesTool.msgNavMode}" title=" #{PrivateMessagesTool.msgNavMode}" 
	 	    rendered="!(PrivateMessagesTool.msgNavMode == 'pvt_received' || PrivateMessagesTool.msgNavMode == 'pvt_sent' || PrivateMessagesTool.msgNavMode == 'pvt_deleted' || PrivateMessagesTool.msgNavMode == 'pvt_drafts' || PrivateMessagesTool.msgNavMode == 'pvt_scheduler'))"/>
		  <h:commandLink action="#{PrivateMessagesTool.processDisplayForum}" value="#{msgs[PrivateMessagesTool.msgNavMode]}" title=" #{msgs[PrivateMessagesTool.msgNavMode]}" 
	 	    rendered="(PrivateMessagesTool.msgNavMode == 'pvt_received' || PrivateMessagesTool.msgNavMode == 'pvt_sent' || PrivateMessagesTool.msgNavMode == 'pvt_deleted' || PrivateMessagesTool.msgNavMode == 'pvt_drafts' || PrivateMessagesTool.msgNavMode == 'pvt_scheduler'))"/>
		   /
		  <h:outputText value="#{msgs.pvt_delcon}" />
		  </h3>
		</div>	
		<sakai:tool_bar_message value="#{msgs.pvt_delcon}" />
		
		<h:messages styleClass="alertMessage" id="errorMessages" rendered="#{! empty facesContext.maximumSeverity}"/> 

    <sakai:panel_titled title="">
	  <h:dataTable styleClass="listHier line nolines" id="pvtmsgdel" width="100%"  value="#{PrivateMessagesTool.selectedDeleteItems}" var="delItems" cellspacing="0" cellpadding="0">   
		  <h:column>
		    <f:facet name="header">
		       <h:outputText value="#{msgs.pvt_subject}"/>
		    </f:facet>
		      <h:outputText value=" #{delItems.msg.title}"/>              
		  </h:column>
		  <h:column>
				<f:facet name="header">
					<h:outputText value="" styleClass="bi bi-paperclip" escape="false" />
					<h:outputText value="#{msgs.msg_has_attach}" styleClass="sr-only" />
				</f:facet>
				<h:outputText value="" styleClass="bi bi-paperclip" escape="false" rendered="#{delItems.msg.hasAttachments}" />			 
			</h:column>
		  <h:column>
		    <f:facet name="header">
		       <h:outputText value="#{msgs.pvt_authby}"/>
		    </f:facet>		     		    
		     <h:outputText value="#{delItems.msg.author}"/>
		  </h:column>
		  <h:column>
		    <f:facet name="header">
		       <h:outputText value="#{msgs.pvt_date}"/>
		    </f:facet>
		     <h:outputText value="#{delItems.msg.created}">
		         <f:convertDateTime pattern="#{msgs.date_format}" timeZone="#{PrivateMessagesTool.userTimeZone}" locale="#{PrivateMessagesTool.userLocale}"/>
		     </h:outputText>
		  </h:column>
		</h:dataTable>
		</sakai:panel_titled>

    <sakai:button_bar>
      <h:commandButton action="#{PrivateMessagesTool.processPvtMsgMultiDelete}" value="#{msgs.pvt_delmsgs}" accesskey="s" styleClass="active"/>
      <h:commandButton action="#{PrivateMessagesTool.processPvtMsgCancelToListView}" value="#{msgs.pvt_cancel}" accesskey="x" />
    </sakai:button_bar>
    
		 </h:form>

	</sakai:view>
</f:view>
