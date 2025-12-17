Feature: Product Reviews
  As a customer of multibags online store
  Charles Leclerc wants to create, delete and update the reviews of any product

  Scenario: Should create a review of the product 1
    Given user is logged in the multibags online store
      | email    | ex195604@g.unicamp.br |
      | password | eX#195604             |
    When he selects the option to create a review of the product "1"
      | description | "Produto bacana! Excelente qualidade." |
      | rating      | 5.0                                                      |
      | language    | "pt"                                                     |
    Then the product review should be created successfully

  Scenario: Should not create a review of a product that has been already reviewed by the user
    Given user is logged in the multibags online store
      | email    | ex195604@g.unicamp.br |
      | password | eX#195604             |
    When he selects the option to create a review of the product "1"
      | description | "Gostei tanto que estou avaliando de novo!" |
      | rating      | 5.0                                         |
      | language    | "pt"                                        |
    Then the product review creation should return an error

  # This scenario will not pass because there is a bug in this feature of the application
  Scenario: Should update a review of the product 1
    Given user is logged in the multibags online store
      | email    | ex195604@g.unicamp.br |
      | password | eX#195604             |
    When he selects the option to visualize reviews of the product "1"
    And gets the reviewId
    And he selects the option to update a review of the product "1"
      | description | "Lixo de produto! Não sei como alguém tem coragem de vender isso." |
      | rating      | 1.0                                                                |
      | language    | "pt"                                                               |
    Then the product review should be updated successfully

  Scenario: Should delete a review of the product 1
    Given user is logged in the multibags online store
      | email    | ex195604@g.unicamp.br |
      | password | eX#195604             |
    When he selects the option to visualize reviews of the product "1"
    And gets the reviewId
    And he selects the option to delete a review of the product "1"
    Then the product review should be deleted successfully

  Scenario: Should get an error while deleting an invalid review of the product 1
    Given user is logged in the multibags online store
      | email    | ex195604@g.unicamp.br |
      | password | eX#195604             |
    When he gets an invalid reviewId
    And he selects the option to delete a review of the product "1000"
    Then the product review deletion should throw an error