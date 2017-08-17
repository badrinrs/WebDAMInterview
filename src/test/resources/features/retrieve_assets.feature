Feature: Retrieve Assets

  Background:
    Given I use following credentials
      |client_id|4|
      |client_secret|4ovGa5yXfHnWR47wGRVUfKlDTBxC3WQtnkmO5sgs|
      |auth_token_url|http://interview-testing-api.webdamdb.com/oauth/token|
      |grant_type    |client_credentials                                   |
    When I make api call
    Then I get access_token to be used later

  Scenario: Validate Asset Search without asset_id param
    When I make an asset search api with assetId ""
    Then I validate search results are as follows
      |assetId|thumbnail|keywords|dateModified|dateCreated|text|
      |12341  |http://interview-testing-api.webdamdb.com/images/apples.jpg|"apple", "red", "pink"|1483275313|1494287713|an apple a day keeps the doctor away|
      |12342  |http://interview-testing-api.webdamdb.com/images/old-man-smile.jpg|"human", "happy", "pink"|1494287713|1493285713|a lovely old man smiling|
      |12343  |http://interview-testing-api.webdamdb.com/images/red-boat.jpg     |"boat", "cherry", "red"|1483275313|1483285713|This cherry red boat from 1986 is one of a kind|
      |12344  |http://interview-testing-api.webdamdb.com/images/red-apple.jpg    |"apple", "cherry", "red"|1494285713|1483285713|Esa es una bonita Manzana|
      |12345  |http://interview-testing-api.webdamdb.com/images/sleepy-human.jpg |"human", "sleepy"|1494286666|1483275313|¿Estás cansado?|

  Scenario: Validate Asset Search with valid asset_id param
    When I make an asset search api with assetId "12345"
    Then I validate search results are as follows
      |assetId|thumbnail|keywords|dateModified|dateCreated|text|
      |12345  |http://interview-testing-api.webdamdb.com/images/sleepy-human.jpg |"human", "sleepy"|1494286666|1483275313|¿Estás cansado?|

  Scenario: Validate Asset Search with unknown asset_id param
    When I make an asset search api with assetId "11111"
    Then I validate empty search response

  Scenario: Validate Asset Search with unknown asset_id param
    When I make an asset search api with assetId "abc"
    Then I validate empty search response