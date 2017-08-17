Feature: WebDam Interview Authenticate

  #Assumption: on logout, the access token becomes invalid and a response logged_in is set to false

  Background:
    Given I use following credentials
      |client_id|4|
      |client_secret|4ovGa5yXfHnWR47wGRVUfKlDTBxC3WQtnkmO5sgs|
      |auth_token_url|http://interview-testing-api.webdamdb.com/oauth/token|
      |grant_type    |client_credentials                                   |
    When I make api call
    Then I get access_token to be used later

  Scenario: Validate Login Feature
    When I use previously created access_token to login
    Then Login was a success

  Scenario: Validate Logout Feature
    When I use previously created access_token to logout
    Then Logout was a success



