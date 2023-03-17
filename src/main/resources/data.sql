
--default users
INSERT INTO `users` VALUES (0  ,'system',  'default');
INSERT INTO `users` VALUES (100,'Thomas','user1');
INSERT INTO `users` VALUES (101,'Mathew','user2');
INSERT INTO `users` VALUES (102,'Alex' , 'user3');

--default ingredients
INSERT INTO `ingredients` VALUES (101,'Cheese');
INSERT INTO `ingredients` VALUES (102,'Paprika');
INSERT INTO `ingredients` VALUES (103,'Salami');
INSERT INTO `ingredients` VALUES (104,'Pineapple');
INSERT INTO `ingredients` VALUES (105,'Potato');
INSERT INTO `ingredients` VALUES (106,'Salt');
INSERT INTO `ingredients` VALUES (107,'Salmon');
INSERT INTO `ingredients` VALUES (108,'Onion');
INSERT INTO `ingredients` VALUES (109,'Garlic');
INSERT INTO `ingredients` VALUES (110,'Chicken');
INSERT INTO `ingredients` VALUES (111,'Egg');
INSERT INTO `ingredients` VALUES (112,'Mustard');
INSERT INTO `ingredients` VALUES (113,'Lettuce');


----Instructions----------
INSERT INTO `INSTRUCTION` VALUES (1,'No Garlic');
INSERT INTO `INSTRUCTION` VALUES (2,'No Paprika');
INSERT INTO `INSTRUCTION` VALUES (3,'Lots of Pineapple');
INSERT INTO `INSTRUCTION` VALUES (4,'Lots of Garlic');
INSERT INTO `INSTRUCTION` VALUES (5,'No Paprika');
INSERT INTO `INSTRUCTION` VALUES (6,'Two eggs');
INSERT INTO `INSTRUCTION` VALUES (7,'Lots of potatos');





--default Recipes
INSERT INTO `recipe` VALUES (101,'default','vegetarian','Pineapple Pizza',4,0);
INSERT INTO `recipe` VALUES (102,'default','vegetarian','Salami Pizza',2,0);
INSERT INTO `recipe` VALUES (103,'default','vegetarian','Potato Pizza',2,0);
INSERT INTO `recipe` VALUES (104,'default','non-vegetarian','Omlette',5,0);

----pineapple pizza
INSERT INTO `RECIPE_INGREDIENTS` VALUES (101,104);
INSERT INTO `RECIPE_INGREDIENTS` VALUES (101,101);
INSERT INTO `RECIPE_INGREDIENTS` VALUES (101,106);
--
--Salami pizza
INSERT INTO `RECIPE_INGREDIENTS` VALUES (102,101);
INSERT INTO `RECIPE_INGREDIENTS` VALUES (102,103);
INSERT INTO `RECIPE_INGREDIENTS` VALUES (102,105);

--Potato pizza
INSERT INTO `RECIPE_INGREDIENTS` VALUES (103,112);
INSERT INTO `RECIPE_INGREDIENTS` VALUES (103,109);
INSERT INTO `RECIPE_INGREDIENTS` VALUES (103,105);


--Omlette
INSERT INTO `RECIPE_INGREDIENTS` VALUES (104,111);
INSERT INTO `RECIPE_INGREDIENTS` VALUES (104,109);
INSERT INTO `RECIPE_INGREDIENTS` VALUES (104,106);
--
--instruction for pineapple pizza
INSERT INTO `RECIPE_INSTRUCTIONS` VALUES (101,1);
INSERT INTO `RECIPE_INSTRUCTIONS` VALUES (101,2);
INSERT INTO `RECIPE_INSTRUCTIONS` VALUES (101,3);

--instruction for omlette
INSERT INTO `RECIPE_INSTRUCTIONS` VALUES (104,4);
INSERT INTO `RECIPE_INSTRUCTIONS` VALUES (104,5);
--
-------------------------------------------------------------------------------------------------------------------------
--
----Pre-create 2 recipes for user1
INSERT INTO `recipe` VALUES (106,'default','non-vegetarian','Spanish Omlette',2,100);
INSERT INTO `recipe` VALUES (107,'default','vegetarian','Potato stew',1,100);
--
--
----spanish omlette
INSERT INTO `RECIPE_INGREDIENTS` VALUES (106,111);
INSERT INTO `RECIPE_INGREDIENTS` VALUES (106,101);
--
----potato stew
INSERT INTO `RECIPE_INGREDIENTS` VALUES (107,105);
INSERT INTO `RECIPE_INGREDIENTS` VALUES (107,106);
--
----instruction for spanish omlette
INSERT INTO `RECIPE_INSTRUCTIONS` VALUES (106,6);
----instruction for potato stew
INSERT INTO `RECIPE_INSTRUCTIONS` VALUES (107,7);
