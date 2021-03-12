
-- Consistent Data Flow 1

INSERT INTO public.company (id, creation_time, update_time, status_type, deleted, deletion_time, name, address, phone, email_address, website_url) VALUES ('38450baf-231c-4894-adfa-816d1dbfc58e', '2020-06-18 00:14:58.652000', '2020-06-18 00:14:58.652000', 'CREATED', false, null, 'Legros and Sons', 'Apt. 370 02523 Tomiko Park, East Troy, WI 00052-8556', '599.646.4260 x725', 'melda.quitzon@yahoo.com', 'www.cyril-cummerata.io');

INSERT INTO public.restaurant (id, creation_time, update_time, status_type, deleted, deletion_time, name, address, phone, employee_count, company_id) VALUES ('388f2314-bd3c-4120-9115-f24d69a64c99', '2020-06-18 00:14:58.652000', '2020-06-18 00:14:58.652000', 'CREATED', false, null, 'dignissimos', '48629 Myrle Course, Danialbury, WI 93951', '(293) 115-8224', 294, '38450baf-231c-4894-adfa-816d1dbfc58e');

INSERT INTO public.menu (id, creation_time, update_time, status_type, deleted, deletion_time, name, menu_type, restaurant_id) VALUES ('f51fa84a-c5fe-4534-a1f1-b16a001734a8', '2020-06-18 00:14:58.652000', '2020-06-18 00:14:58.652000', 'CREATED', false, null, 'maxime', 'cumque', '388f2314-bd3c-4120-9115-f24d69a64c99');

INSERT INTO public.food (id, creation_time, update_time, status_type, deleted, deletion_time, name, vegetable, price, image_url) VALUES ('3096cf55-f286-431f-90ae-131e52bcebc7', '2020-06-18 00:14:58.652000', '2020-06-18 00:14:58.652000', 'CREATED', false, null, 'Pasta Carbonara', false, 28.36, 'www.elly-corwin.co');

INSERT INTO public.menu_food (id, creation_time, update_time, status_type, deleted, deletion_time, extended, extended_price, menu_id, food_id) VALUES ('c63f492c-ea3d-475a-b263-975f849b26c0', '2020-06-18 00:14:58.652000', '2020-06-18 00:14:58.652000', 'CREATED', false, null, true, 37.11, 'f51fa84a-c5fe-4534-a1f1-b16a001734a8', '3096cf55-f286-431f-90ae-131e52bcebc7');

-- Consistent Data Flow 2

INSERT INTO public.company (id, creation_time, update_time, status_type, deleted, deletion_time, name, address, phone, email_address, website_url) VALUES ('9f16c55f-56dc-43d7-958c-2ed0aa7bc0e5', '2020-06-18 00:18:07.755000', '2020-06-18 00:18:07.755000', 'CREATED', false, null, 'Sporer, Funk and Greenholt', 'Suite 840 22635 Jerde Alley, New Williamtown, MI 18010-0760', '1-878-519-5145 x51301', 'berneice.murphy@hotmail.com', 'www.virgilio-kris.io');

INSERT INTO public.restaurant (id, creation_time, update_time, status_type, deleted, deletion_time, name, address, phone, employee_count, company_id) VALUES ('9d7cfe42-e7dd-44b5-b01b-2fbaa6dd619e', '2020-06-18 00:18:07.755000', '2020-06-18 00:18:07.755000', 'CREATED', false, null, 'et', 'Suite 741 69583 Fadel Mall, Ullrichberg, NY 07740', '(331) 036-4312 x1039', 32, '9f16c55f-56dc-43d7-958c-2ed0aa7bc0e5');

INSERT INTO public.menu (id, creation_time, update_time, status_type, deleted, deletion_time, name, menu_type, restaurant_id) VALUES ('8b03175c-af6c-4cab-a958-70d53369fe5c', '2020-06-18 00:18:07.755000', '2020-06-18 00:18:07.755000', 'CREATED', false, null, 'magni', 'et', '9d7cfe42-e7dd-44b5-b01b-2fbaa6dd619e');

INSERT INTO public.food (id, creation_time, update_time, status_type, deleted, deletion_time, name, vegetable, price, image_url) VALUES ('1a713ef5-0078-452d-958e-770fbb797797', '2020-06-18 00:18:07.755000', '2020-06-18 00:18:07.755000', 'CREATED', false, null, 'Scotch Eggs', false, 20.57, 'www.tonita-langosh.biz');

INSERT INTO public.menu_food (id, creation_time, update_time, status_type, deleted, deletion_time, extended, extended_price, menu_id, food_id) VALUES ('c91160e7-0820-425f-8def-17442672c48a', '2020-06-18 00:18:07.755000', '2020-06-18 00:18:07.755000', 'CREATED', false, null, true, 26.56, '8b03175c-af6c-4cab-a958-70d53369fe5c', '1a713ef5-0078-452d-958e-770fbb797797');