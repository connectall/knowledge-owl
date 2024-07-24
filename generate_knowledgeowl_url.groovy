/*
- Author: Johnathan McGowan
- Date: April 11, 2024
- Company: Broadcom
- Purpose: Use a collection of fields from an Article Version to create a full web link to the Article Version
- Execution Policy: PreMapValues
- Field mapping setup:
    1. Map the KnowledgeOwl Article Version's current_version_en_title to the title field of the destination application
    2. Map the KnowledgeOwl Article Version's project_id to any string or rich text field on the destination application
    3. Map the KnowledgeOwl Article Version's version_number to _DO_NOT_SYNC
*/
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import com.go2group.connectall.config.model.ConnectAllConfig
import com.go2group.connectall.config.model.ApplicationInstance

Logger logger  = LogManager.getLogger("com.go2group.connectall.model.transformer.model.api.knowledge-owl-url");
logger.info("~~~Processing KnowledgeOwl URL Rule~~~")

//The URL rule should only apply in a single direction. (Source -> Destination)
//Set up the variables needed to obtain the source and destination connections for the current sync
def srcId = message.getSessionProperty('source')
def destId = message.getSessionProperty('destination')
def connectionName = message.getSessionProperty('connection.name')
ConnectAllConfig caConfig = message.getSessionProperty('config')
ApplicationInstance srcappInstance = caConfig.getApplication(caConfig.getApplicationPair(connectionName), srcId)
ApplicationInstance destappInstance = caConfig.getApplication(caConfig.getApplicationPair(connectionName), destId)

//Fetch the current source and destination application names
def _srcAppKey = srcappInstance.getType()
def _destAppKey = destappInstance.getType()

//Define which application this rule should apply to 
def _srcAppToApplyRule = "ua-knowledge-owl";

logger.info("URL rule should only apply to: " + _srcAppToApplyRule);
logger.info("Current source application: " + _srcAppKey)

//Below is the actual business logic
if(_srcAppKey.equalsIgnoreCase(_srcAppToApplyRule)) {
	logger.info("Source matches. Proceeding with creating a URL.");
	
	//define the field that will be overwritten in the mapping
	def fieldToOverwrite = "project_id";
	
	//*** CHANGE the baseURL field below to the actual URL of the Knowledge Base
	//Final URL should look like <a href="https://app.knowledgeowl.com/kb/article/id/project_id/aid/article_id/language/en/version/version_id">Link to Version Number</a>
	def baseURL = "https://app.knowledgeowl.com/kb/article/id/"
	def projectId = message.payload.getSingleValueField("project_id");
	def articleId = message.payload.getSingleValueField("article_id");
	def versionId = message.payload.getSingleValueField("id");
	def title = message.payload.getSingleValueField("current_version_en_title");
	def versionNumber = message.payload.getSingleValueField("version_number");
	def newURL =  "Link to Article: <a href=\"" + baseURL + projectId + "/aid/" + articleId + "/language/en/version/" + versionId + "\">" + title + " | Version " + versionNumber + "</a>";
	logger.info("URL is: " + newURL);
    
	//overwrite the chosen field with the new URL
	message.payload.setSingleValueField(fieldToOverwrite,newURL);
} else {
    logger.info("Source does not match. Skipping URL rule.")
}

return message.payload;
