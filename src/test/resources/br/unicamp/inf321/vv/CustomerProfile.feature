Feature: Customer Profile
  As a customer of multibags online store
  Fernando Alonso wants to visualize his profile
  This way he can check his personal information (basic data, address, etc)

  Scenario: Should show customer profile details
    Given Fernando Alonso is logged in the multibags online store
      | email    | ex195604@g.unicamp.br |
      | password | eX#195604             |
    When he selects the option to check his profile
    Then his personal information should be shown successfully