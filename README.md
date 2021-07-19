# pen-backend


| Action Description |Verb | URI | Input | Output |
|:----------------: | --- | ----| ----- | -------|
| Candidate registration | POST | /rest/candidate | Name, address, mobile | - |
| Scribe registration | POST | /rest/scribe | Name, address, mobile, languages, qualification | - |
| Scribe updates availability time | PUT | /rest/scribe | Availability time and location proximity | - |
|Candidate searches for Scribe|GET|/rest/scribe|Exam date, schedule, language,|location|Number of scribes available for the schedule and location (Will consider scribe location proximity for search)|
|Scribe registration||POST||/rest/scribe||Name, address, mobile, languages, qualification|-|
|Scribe updates availability time|PUT|/rest/scribe|Availability time and location proximity|-|
|Candidate searches for Scribe|GET|/rest/scribe|Exam date, schedule, language,location|Number of scribes available for the schedule and location (Will consider scribe location proximity for search)|
|Candidate raises request for scribe service|POST|/rest/exam/requests|Candidate exam schedule, language and location|Request id|
|Scribe lists all his requests|GET|/rest/exam/requests|Scribe id|Relevant list of requests with candidate and exam details|
|Scribe accepts the request|POST|/rest/exam/events|Request id|Exam event with scribe and candidate details|-|
|Candidate/Scribe receives the notification|GET|/rest/exam/notifications|Updated notifications with OTP details|-|
|Scribe validates OTP with Candidate on the day of exam.|POST|/rest/exam/events/{id}|OTP details|Update response|
|Exam completes|POST|/rest/exam/event/{id}|feedback|-|

### Scenarios

Candidate registers into the system
Scribe registers into the system
Scribe updates his normal availability times and location preference
Candidate searches for scribe based on exam date, language, and place
Gets count of scribes available. Raises request. Request is sent to all relevant scribes.
Scribe gets a notification and accepts it.
Candidate gets notification of acceptance with OTP.
On the day of exam
Scribe gets an OTP.
Validates the OTP with the candidate
On completion of exam, both scribe & candidate enter their feedback.


#### /rest/candidates
POST - candidate registration

#### /rest/scribes
POST - scribe registration
PUT - update scribe with availability and location preferences
GET - get all scribes based on timings and location criteria


#### /rest/requests
POST - candidate raises request for scribe service
GET - list all relevant requests for the given scribe

#### /rest/exams/events
POST - based on request id, an exam event created
/rest/exam/events/{id}
POST - candidate validates OTP with candidate
- scribe/candidate update with feedback

#### /rest/notifications
GET - get list of notifications based on given scribe/candidate
