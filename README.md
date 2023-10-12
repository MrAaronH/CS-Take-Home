# CS-Take-Home

This repository contains Groovy scripts that are intended to be used with the Jira Data Center ScriptRunner plugin scripting console

## Table of Contents

- [Installation](#installation)
- [Scripts](#scripts)
    - [Count Custom Field Usage](#assessment_1_total_custom_field_usage_count)
    - [Count Project Custom Field Usage](#assessment_2_project_custom_field_usage_count)
    - [Copy Multiselect Field](#assessment_3_copy_multi-select_field_to_another_multi_select_field)
- [Using the scripts](#using-the-scripts)
- [License](#license)

## Installation

Clone the repository to your local machine

```bash
git clone https://github.com/MrAaronH/CS-Take-Home
```

## Scripts

### Assessment 1 Total Custom Field Usage Count

This script counts the total number of times a custom field has been used across an instance.

- **File**: `custom_field_usage.groovy`
- **Usage**: Run this script via ScriptRunner's Script Console.
- **Parameters**:
  - `customfield_XXXXX`: Replace this with the relevant custom field ID.
  - `adminUsername`: Optional - add username here to run ad a different admin user account.

#### Sample Log Output
```
Custom field Story Points has been used 5 times.
```

### Assessment 2 Project Custom Field Usage Count

This script counts the number of times a custom field has been used on a per-project basis then outputs the results in a log file

- **File**: `project_custom_field_usage.groovy`
- **Usage**: Run this script via ScriptRunner's Script Console. Results can be viewed in the Console Log or in the identified logger.
- **Parameters**:
  - `customfield_XXXXX`: Replace this with the relevant custom field ID.
  - `com.acme.ProjectCustomField`: Optional - replace this with a preferred logger if desired.

#### Sample Log Output
```
Custom field Story Points usage summary:
KP 1
SP 1
SCRUM 1
```

### Assessment 3 Copy Values Of Multi-Select Field To Another Multi-Select Field

This script copies the values of one multi-select field to another multi-select field on the same issue. A JQL query is used to filter issue scope. Custom mapping of source values to target values 
can be configured when applyMapping is set to true.

- **File**: `copy_multi_select_value.groovy`
- **Usage**: Run this script via ScriptRunner's Script Console. Set a custom mapping and toogle applyMapping feature flag in order to customize mapping of field values.
- **Parameters**:
  - `customfield_XXXXX`: Replace this with the source custom field ID.
  - `customfield_YYYYY`: Replace this with the target custom field ID.
  - `project = 'Your Project'"`: Replace this with a filtering JQL statement.
  - `applyMapping = false`: Set to true if planning on utilizing a custom mapping.
  - `'Source Value 1': 'Target Value A'`: Use these fields to define relationship between individual field values when utilizing a custom mapping.
 
#### Sample Output
```
[DocumentIssueImpl[IssueKey=SP-1]]
```

## Usage
1. Navigate to a Jira Data Center instance's ScriptRunner Script Console.
2. Copy and paste desired Groovy script from this repository into the console.
3. Modify parameters as necessary.
4. Execute script and stand by for output.

**Note**: You must have administrative permissions to run these scripts

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details.
