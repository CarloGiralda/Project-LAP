alter table users
   alter column id set default nextval('user_sequence');
INSERT INTO users(
	enabled, locked, app_user_role, contact, email, first_name, last_name, password, residence)
	VALUES (true, false,  'USER', '321321321', 'bordin.2081387@studenti.uniroma1.it', 'Gianmarco', 'Bordin', '$2a$10$3GQ95ipp6VMEtDsE5Jc59uwK1I9KSHghGV4hJzWDni03dAEnwwW0O', 'roma'),
 (true, false, 'USER', '321321323', 'gabriele.lerani2000@gmail.com', 'Gabriele', 'Lerani', '$2a$10$3GQ95ipp6VMEtDsE5Jc59uwK1I9KSHghGV4hJzWDni03dAEnwwW0O', 'firenze'),
 (true, false,  'USER', '321321329', 'giralda.1903088@studenti.uniroma1.it', 'Carlo', 'Giralda', '$2a$10$3GQ95ipp6VMEtDsE5Jc59uwK1I9KSHghGV4hJzWDni03dAEnwwW0O', 'milano');