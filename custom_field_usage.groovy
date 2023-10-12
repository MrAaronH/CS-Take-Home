import com.atlassian.jira.component.ComponentAccessor
import com.atlassian.jira.issue.CustomFieldManager
import com.atlassian.jira.issue.search.SearchProvider
import com.atlassian.jira.jql.parser.JqlQueryParser
import com.atlassian.jira.web.bean.PagerFilter
import com.atlassian.jira.user.ApplicationUser
import com.atlassian.jira.user.util.UserManager
import com.atlassian.jira.bc.issue.search.SearchService

// Initialize required components
CustomFieldManager customFieldManager = ComponentAccessor.getCustomFieldManager()
SearchProvider searchProvider = ComponentAccessor.getComponent(SearchProvider)
JqlQueryParser jqlQueryParser = ComponentAccessor.getComponent(JqlQueryParser)
UserManager userManager = ComponentAccessor.getUserManager()
SearchService searchService = ComponentAccessor.getComponent(SearchService)

// Custom field ID and optional admin username
String customFieldId = "customfield_XXXXX"  // Replace with your Custom Field ID
String adminUsername = ""  // Optional: replace with the username of an admin

// Retrieve the user for search (admin user if specified, otherwise the logged-in user)
ApplicationUser searchUser = adminUsername ? userManager.getUserByName(adminUsername)
                                            : ComponentAccessor.getJiraAuthenticationContext().getLoggedInUser()

if (searchUser == null) {
    return "User not found. Please check the admin username or make sure you are logged in."
}

// Fetches custom field name for output readability 
def customField = customFieldManager.getCustomFieldObject(customFieldId)

// Prepares customFieldId for JQL search
def idNumber = customFieldId.replaceAll("[^\\d]", "")
log.debug("Custom field Id number ${idNumber}")


// Retrieve the custom field by ID
def targetCustomField = customFieldManager.getCustomFieldObject(customFieldId)

if (targetCustomField) {
    // Create a JQL query to find all issues where the custom field is not empty
    def query = jqlQueryParser.parseQuery("cf[${idNumber}] is not EMPTY")

    // Perform the search and get the issue count
    def searchQuery = searchService.parseQuery(searchUser, query.queryString)
    def results = searchService.search(searchUser, searchQuery.getQuery(), PagerFilter.getUnlimitedFilter())
    def issueCount = results.total

    // Generate the report
    return "Custom Field ID: ${customFieldId}\nTotal times used: ${issueCount}"
} else {
    return "Custom Field with ID '${customFieldId}' not found."
}
