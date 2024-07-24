/*
- Author: Johnathan McGowan
- Date: May 22, 2024
- Company: Broadcom
- Purpose: Prefix a set value to the KnowlegeOwl title before syncing it to a destination
- Execution Policy: PreMapValues
- Field mapping setup:
    1. Map the KnowledgeOwl Article Verion's current_version_en_title field to the desired field in the destination application.
    2. Map the KnowledgeOwl Article Verion's version_number to _DO_NOT_SYNC
*/
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import com.go2group.connectall.config.model.ConnectAllConfig
import com.go2group.connectall.config.model.ApplicationInstance
import com.go2group.connectall.config.model.DataDictionary
import com.go2group.connectall.config.model.bean.FieldValue
import com.go2group.connectall.config.model.bean.Name

Logger logger  = LogManager.getLogger("com.go2group.connectall.model.transformer.model.api.knowledge-owl-title");
logger.info("~~~Processing KnowledgeOwl Title Prefix Rule~~~")

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
def _srcAppToApplyRule = "knowledge-owl";

logger.info("Prefix rule should only apply to: " + _srcAppToApplyRule);
logger.info("Current source application: " + _srcAppKey)

//Below is the actual business logic
if(_srcAppKey.equalsIgnoreCase(_srcAppToApplyRule)) {
	logger.info("Source matches. Proceeding with prefixing the title.");
	
	// fetch the current title
  	def fieldToOverwrite = "current_version_en_title";
	
	def title = message.payload.getSingleValueField("current_version_en_title");
	def versionNumber = message.payload.getSingleValueField("version_number");
	def prefixedTitle = "Edit KB Article | v" + versionNumber + " | " + title;
	logger.info("Prefixed title is: " + prefixedTitle);
    
	//overwrite the chosen field with the new URL
	message.payload.setSingleValueField(fieldToOverwrite,prefixedTitle);
} else {
    logger.info("Source does not match. Skipping Prefix rule.")
}

return message.payload;
