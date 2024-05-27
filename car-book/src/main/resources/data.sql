alter table bookings
   alter column bid set default nextval('bookings_seq');
alter table carsubscription
      alter column id set default nextval('carsubscription_seq');
INSERT INTO bookings(
	 cid, description, from_day, from_hour, made_date, to_day, to_hour, username)
	VALUES ( 1, '', '02-02-2024', '15:00', '01-01-2024', '02-02-2024', '18:00', 'giralda.1903088@studenti.uniroma1.it'),
	 ( 2, '', '06-02-2024', '23:00', '05-02-2024', '07-02-2024', '01:00', 'bordin.2081387@studenti.uniroma1.it'),
	 ( 3, '', '03-01-2024', '07:00', '01-01-2024', '03-01-2024', '14:00', 'gabriele.lerani2000@gmail.com'),
	 ( 4, '', '08-01-2024', '10:00', '06-01-2024', '08-01-2024', '12:00', 'giralda.1903088@studenti.uniroma1.it'),
	 ( 5, '', '01-03-2024', '01:00', '18-01-2024', '01-03-2024', '23:00', 'bordin.2081387@studenti.uniroma1.it'),
	 ( 6, '','11-03-2024', '18:00', '01-03-2024', '12-03-2024', '18:00', 'gabriele.lerani2000@gmail.com'),
     ( 7, '','11-03-2022', '18:00', '10-03-2022', '14-03-2022', '18:00', 'gabriele.lerani2000@gmail.com'),
     ( 8, '','05-04-2023', '18:00', '04-04-2023', '07-04-2023', '18:00', 'gabriele.lerani2000@gmail.com'),
     ( 9, '','23-07-2023', '18:00', '22-07-2023', '25-07-2023', '18:00', 'gabriele.lerani2000@gmail.com');
INSERT INTO carsubscription(
	car_id,  user_id)
	VALUES (1,  'giralda.1903088@studenti.uniroma1.it'),
	 (2,  'bordin.2081387@studenti.uniroma1.it'),
	(3,  'gabriele.lerani2000@gmail.com');