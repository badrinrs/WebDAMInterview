Feature: Validate Assets Search

  #Assumption: query to be alphanumeric and return if value matches atleast part of string in keywords

  #Assumption: valid values of sort:
  # aut - default (ascending)
  # asc - ascending
  # desc - descending

  #Assumption: limit can accept only unsigned int

  Background:
    Given I use following credentials
      |client_id|4|
      |client_secret|4ovGa5yXfHnWR47wGRVUfKlDTBxC3WQtnkmO5sgs|
      |auth_token_url|http://interview-testing-api.webdamdb.com/oauth/token|
      |grant_type    |client_credentials                                   |
    When I make api call
    Then I get access_token to be used later

  Scenario: Validate Asset Search without optional params
    When I make an asset search api without any params
    Then I validate search results are as follows
      |assetId|thumbnail|keywords|dateModified|dateCreated|text|
      |12341  |http://interview-testing-api.webdamdb.com/images/apples.jpg|"apple", "red", "pink"|1483275313|1494287713|an apple a day keeps the doctor away|
      |12342  |http://interview-testing-api.webdamdb.com/images/old-man-smile.jpg|"human", "happy", "pink"|1494287713|1493285713|a lovely old man smiling|
      |12343  |http://interview-testing-api.webdamdb.com/images/red-boat.jpg     |"boat", "cherry", "red"|1483275313|1483285713|This cherry red boat from 1986 is one of a kind|
      |12344  |http://interview-testing-api.webdamdb.com/images/red-apple.jpg    |"apple", "cherry", "red"|1494285713|1483285713|Esa es una bonita Manzana|
      |12345  |http://interview-testing-api.webdamdb.com/images/sleepy-human.jpg |"human", "sleepy"|1494286666|1483275313|¿Estás cansado?|

  Scenario: Validate Asset Search with query param "apple" only
    When I make an asset search api call with parameters
      |query|apple|
    Then I validate search results are as follows
      |assetId|thumbnail|keywords|dateModified|dateCreated|text|
      |12341  |http://interview-testing-api.webdamdb.com/images/apples.jpg|"apple", "red", "pink"|1483275313|1494287713|an apple a day keeps the doctor away|
      |12344  |http://interview-testing-api.webdamdb.com/images/red-apple.jpg    |"apple", "cherry", "red"|1494285713|1483285713|Esa es una bonita Manzana|

  Scenario: Validate Asset Search with limit param "10" only
    When I make an asset search api call with parameters
      |limit|10|
    Then I validate search results are as follows
      |assetId|thumbnail|keywords|dateModified|dateCreated|text|
      |12341  |http://interview-testing-api.webdamdb.com/images/apples.jpg|"apple", "red", "pink"|1483275313|1494287713|an apple a day keeps the doctor away|
      |12342  |http://interview-testing-api.webdamdb.com/images/old-man-smile.jpg|"human", "happy", "pink"|1494287713|1493285713|a lovely old man smiling|
      |12343  |http://interview-testing-api.webdamdb.com/images/red-boat.jpg     |"boat", "cherry", "red"|1483275313|1483285713|This cherry red boat from 1986 is one of a kind|
      |12344  |http://interview-testing-api.webdamdb.com/images/red-apple.jpg    |"apple", "cherry", "red"|1494285713|1483285713|Esa es una bonita Manzana|
      |12345  |http://interview-testing-api.webdamdb.com/images/sleepy-human.jpg |"human", "sleepy"|1494286666|1483275313|¿Estás cansado?|

  Scenario: Validate Asset Search with sort param "asc" only
    When I make an asset search api call with parameters
      |sort|asc|
    Then I validate search results are as follows in the same order
      |assetId|thumbnail|keywords|dateModified|dateCreated|text|
      |12341  |http://interview-testing-api.webdamdb.com/images/apples.jpg|"apple", "red", "pink"|1483275313|1494287713|an apple a day keeps the doctor away|
      |12342  |http://interview-testing-api.webdamdb.com/images/old-man-smile.jpg|"human", "happy", "pink"|1494287713|1493285713|a lovely old man smiling|
      |12343  |http://interview-testing-api.webdamdb.com/images/red-boat.jpg     |"boat", "cherry", "red"|1483275313|1483285713|This cherry red boat from 1986 is one of a kind|
      |12344  |http://interview-testing-api.webdamdb.com/images/red-apple.jpg    |"apple", "cherry", "red"|1494285713|1483285713|Esa es una bonita Manzana|
      |12345  |http://interview-testing-api.webdamdb.com/images/sleepy-human.jpg |"human", "sleepy"|1494286666|1483275313|¿Estás cansado?|

  Scenario: Validate Asset Search with sort param "desc" only
    When I make an asset search api call with parameters
      |sort|desc|
    Then I validate search results are as follows in the same order
      |assetId|thumbnail|keywords|dateModified|dateCreated|text|
      |12345  |http://interview-testing-api.webdamdb.com/images/sleepy-human.jpg |"human", "sleepy"|1494286666|1483275313|¿Estás cansado?|
      |12344  |http://interview-testing-api.webdamdb.com/images/red-apple.jpg    |"apple", "cherry", "red"|1494285713|1483285713|Esa es una bonita Manzana|
      |12343  |http://interview-testing-api.webdamdb.com/images/red-boat.jpg     |"boat", "cherry", "red"|1483275313|1483285713|This cherry red boat from 1986 is one of a kind|
      |12342  |http://interview-testing-api.webdamdb.com/images/old-man-smile.jpg|"human", "happy", "pink"|1494287713|1493285713|a lovely old man smiling|
      |12341  |http://interview-testing-api.webdamdb.com/images/apples.jpg|"apple", "red", "pink"|1483275313|1494287713|an apple a day keeps the doctor away|

  Scenario: Validate Asset Search with sort param "asc" and query param "a" only
    When I make an asset search api call with parameters
      |sort|asc|
      |query|a|
    Then I validate search results are as follows in the same order
      |assetId|thumbnail|keywords|dateModified|dateCreated|text|
      |12341  |http://interview-testing-api.webdamdb.com/images/apples.jpg|"apple", "red", "pink"|1483275313|1494287713|an apple a day keeps the doctor away|
      |12342  |http://interview-testing-api.webdamdb.com/images/old-man-smile.jpg|"human", "happy", "pink"|1494287713|1493285713|a lovely old man smiling|
      |12343  |http://interview-testing-api.webdamdb.com/images/red-boat.jpg     |"boat", "cherry", "red"|1483275313|1483285713|This cherry red boat from 1986 is one of a kind|
      |12344  |http://interview-testing-api.webdamdb.com/images/red-apple.jpg    |"apple", "cherry", "red"|1494285713|1483285713|Esa es una bonita Manzana|
      |12345  |http://interview-testing-api.webdamdb.com/images/sleepy-human.jpg |"human", "sleepy"|1494286666|1483275313|¿Estás cansado?|

  Scenario: Validate Asset Search with sort param "asc" and query param "apple" only
    When I make an asset search api call with parameters
      |sort|asc|
      |query|apple|
    Then I validate search results are as follows in the same order
      |assetId|thumbnail|keywords|dateModified|dateCreated|text|
      |12341  |http://interview-testing-api.webdamdb.com/images/apples.jpg|"apple", "red", "pink"|1483275313|1494287713|an apple a day keeps the doctor away|
      |12344  |http://interview-testing-api.webdamdb.com/images/red-apple.jpg    |"apple", "cherry", "red"|1494285713|1483285713|Esa es una bonita Manzana|

  Scenario: Validate Asset Search with valid sort "asc" and invalid query params "aut" only
    When I make an asset search api call with parameters
      |sort|asc|
      |query|aut|
    Then I validate empty search response

  Scenario: Validate Asset Search with valid sort param "asc", invalid query param "aut" and invalid limit param "0" only
    When I make an asset search api call with parameters
      |sort|asc|
      |query|aut|
      |limit|0  |
    Then I validate empty search response

  Scenario: Validate Asset Search with invalid sort param "asc1", invalid query param "aut" and invalid limit param "0" only
    When I make an asset search api call with parameters
      |sort|aut|
      |query|aut|
      |limit|0  |
    Then I validate empty search response

  Scenario: Validate Asset Search with invalid sort param "1" only
    When I make an asset search api call with parameters
      |sort|1|
    Then I validate error response
      |error|sort invalid|

  Scenario: Validate Asset Search with invalid sort param "asc1" and invalid query param  "aut" only
    When I make an asset search api call with parameters
      |sort|asc1|
      |query|aut|
    Then I validate error response
      |error|sort invalid|


  Scenario: Validate Asset Search with valid sort param "asc", invalid query param "aut" and invalid limit params "-1" only
    When I make an asset search api call with parameters
      |sort|asc|
      |query|aut|
      |limit|-1 |
    Then I validate error response
      |error|limit invalid, has to be unsigned|

  Scenario: Validate Asset Search with invalid sort param "asc1", invalid query param "aut" and invalid limit param "0" only
    When I make an asset search api call with parameters
      |sort|asc1|
      |query|aut|
      |limit|0  |
    Then I validate error response
      |error|sort invalid|

