import com.atlassian.jira.component.ComponentAccessor
import com.atlassian.jira.issue.CustomFieldManager
import com.atlassian.jira.bc.issue.search.SearchService
import com.atlassian.jira.jql.parser.JqlQueryParser
import com.atlassian.jira.web.bean.PagerFilter
import com.atlassian.jira.project.Project
import com.atlassian.jira.project.ProjectManager
import org.apache.log4j.Logger
import org.apache.log4j.Level
import com.atlassian.jira.user.ApplicationUser
import com.atlassian.jira.user.util.UserManager

//If current user lacks access to issues due to Issue Security Schemes, another admin user can be identified using the next two lines
//UserManager userManager = ComponentAccessor.getUserManager()
//ApplicationUser adminUser = userManager.getUserByName("admin") // Replace 'admin' with the actual admin username

//Preferred loggeer can be identified here 
def logging = Logger.getLogger("com.acme.ProjectCustomFieldUsage")
def jqlQueryParser = ComponentAccessor.getComponent(JqlQueryParser.class)
def customFieldManager = ComponentAccessor.getCustomFieldManager()
def SearchService = ComponentAccessor.getComponent(SearchService.class)
def user = ComponentAccessor.getJiraAuthenticationContext().getLoggedInUser()
def projectManager = ComponentAccessor.getProjectManager()

// Update custom field id
String customFieldId = "customfield_XXXXX"

// Fetches custom field name for output readability 
def customField = customFieldManager.getCustomFieldObject(customFieldId)
log.debug("Custom field name ${customField}")

// Prepares customFieldId for JQL search
def idNumber = customFieldId.replaceAll("[^\\d]", "")
log.debug("Custom field Id number ${idNumber}")

//Preparation for log output
StringBuilder summary = new StringBuilder("Custom field ${customField} usage summary:\n")

//Allows capture of log output in ScriptRunner console log use Level.WARN to suppress console output
log.setLevel(Level.INFO)

// Iterate through all projects
projectManager.getProjectObjects().each { Project project ->

    // Performs a search for presence of custom field within each project
    String jql = "project = ${project.key} AND cf[${idNumber}] is not EMPTY"
    def query = jqlQueryParser.parseQuery(jql)
    def searchResults = SearchService.search(user, query, PagerFilter.getUnlimitedFilter())
    //If a seperate admin user is being identified, replace the previous line with next line
    //def searchResults = SearchService.search(adminUser, query, PagerFilter.getUnlimitedFilter())

    int issueCount = searchResults.total

    //Logs project keys and number of uses of custom field
    if (issueCount > 0) {
        String logEntry = "${project.key} ${issueCount}"
        log.debug(logEntry)
        summary.append(logEntry).append('\n')
    }
}

log.info (summary.toString())

return "Logging complete"
