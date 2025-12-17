Feature: List Product Reviews
  As a customer of multibags online store
  Lando Norris wants to visualize the reviews of a product

  Scenario: Should show reviews of a product
    Given user is logged in the multibags online store
      | email    | ex195604@g.unicamp.br |
      | password | eX#195604             |
    When he selects the option to visualize reviews of the product "2"
    Then the product reviews should be shown successfully

  Scenario: Should not show reviews of a invalid product
    Given user is logged in the multibags online store
      | email    | ex195604@g.unicamp.br |
      | password | eX#195604             |
    When he selects the option to visualize reviews of the product "1000"
    Then the product reviews should not be shown successfully
