# GourmetOven App Components Description
Have a brief look at components and its responsibilities to collaborate the GourmetOven App requirements 

### RecipeController
Entry point for Rest API calls where user can access once APP runs on configured port  

* Please refer README.md for list of Rest end points

### ApplicationSecurityConfig
Enables Authentication for Users which allows restriction on CRUD operations for default/user-defined recipes. 

### GourmetOvenService
Service class which provides below responsibilities:

* Validates the rest API call input data receives as DTOs
* Fetch data from DB based on search/requests received
* Validates and verifies the operation is allowed for requested User
* Respond back DTO data to end user

### Repository
Different JPA interfaces created to achieve efficient CRUD operation based User requests:

* IngredientRepository - To List/validate/Map Ingredients.

![img_1.png](img_1.png)

* InstructionRepository - To List/validate/Map instructions to recipe.

![img_2.png](img_2.png)

* RecipeIngredientsMappingRepository - Map existing Ingredients to Recipes on create/update/delete.

![img_3.png](img_3.png)

* RecipeInstructionMappingRepository - Map Instructions to each Recipe.

![img_4.png](img_4.png)
* RecipeRepository - To handle CRUD operations and validate recipes.

![img_5.png](img_5.png)

* UserRepository - To get existing users details.

![img.png](img.png)