import com.atlassian.jira.bc.issue.search.SearchService
import com.atlassian.jira.component.ComponentAccessor
import com.atlassian.jira.issue.Issue
import com.atlassian.jira.issue.ModifiedValue
import com.atlassian.jira.issue.customfields.option.Option
import com.atlassian.jira.issue.util.DefaultIssueChangeHolder
import com.atlassian.jira.jql.parser.JqlQueryParser
import com.atlassian.jira.web.bean.PagerFilter

def customFieldManager = ComponentAccessor.getCustomFieldManager()
def searchService = ComponentAccessor.getComponent(SearchService)
def user = ComponentAccessor.getJiraAuthenticationContext().getLoggedInUser()

String jqlQuery = "project = 'Your Project'" // Modify this JQL as needed
def query = ComponentAccessor.getComponent(JqlQueryParser).parseQuery(jqlQuery)
def results = searchService.search(user, query, PagerFilter.getUnlimitedFilter())

// Flag to control whether to apply mapping
boolean applyMapping = false // Set to false if you don't want to apply mapping

// Define a mapping from source field option IDs to target field option IDs
def valueMapping = [
    'Source Value 1': 'Target Value A',
    'Source Value 2': 'Target Value B',
    // ... add more mappings here
]

results.getResults().each { Issue issue ->
    // Replace with your actual custom field IDs
    String sourceCustomFieldId = "sourcecustomfield_XXXXX"
    String targetCustomFieldId = "targetcustomfield_YYYYY"

    def sourceCustomField = customFieldManager.getCustomFieldObject(sourceCustomFieldId)
    def targetCustomField = customFieldManager.getCustomFieldObject(targetCustomFieldId)

    def sourceValues = issue.getCustomFieldValue(sourceCustomField) as List<Option>
    def targetFieldConfig = targetCustomField.getRelevantConfig(issue)
    def targetOptions = ComponentAccessor.getOptionsManager().getOptions(targetFieldConfig)

    def targetValues = sourceValues.collect { Option sourceOption ->
        if (applyMapping) {
            def mappedValue = valueMapping[sourceOption.getValue()]
            targetOptions.find { it.getValue() == mappedValue }
        } else {
            targetOptions.find { it.getValue() == sourceOption.getValue() }
        }
    }.findAll { it != null }

    if (targetValues) {
        def changeHolder = new DefaultIssueChangeHolder()
        targetCustomField.updateValue(null, issue, new ModifiedValue(issue.getCustomFieldValue(targetCustomField), targetValues), changeHolder)
    }
}
