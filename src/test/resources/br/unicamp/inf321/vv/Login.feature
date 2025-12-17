Feature: Login
  As a customer of multibags online store
  Max Verstappen wants to login in the application
  This way he can buy other products

  Scenario: Login with valid credentials
    Given Max Verstappen is registered on the multibags online store
    When he logs in with his credentials
      | email    | ex195604@g.unicamp.br |
      | password | eX#195604             |
    Then he should be logged in successfully
