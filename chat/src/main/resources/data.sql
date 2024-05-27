alter table chats
   alter column id set default nextval('chat_sequence');
alter table messages
      alter column id set default nextval('message_sequence');
INSERT INTO chats(
	receiver, sender)
	VALUES ('bordin.2081387@studenti.uniroma1.it', 'giralda.1903088@studenti.uniroma1.it'),
           ( 'gabriele.lerani2000@gmail.com', 'bordin.2081387@studenti.uniroma1.it'),
           ( 'giralda.1903088@studenti.uniroma1.it', 'gabriele.lerani2000@gmail.com');
INSERT INTO messages(
	  content, receiver, sender, session_id)
	VALUES ( 'where do we meet?', 'bordin.2081387@studenti.uniroma1.it', 'giralda.1903088@studenti.uniroma1.it', ''),
           (  'at my house', 'giralda.1903088@studenti.uniroma1.it',  'bordin.2081387@studenti.uniroma1.it', ''),
           ( 'be punctual!', 'gabriele.lerani2000@gmail.com',  'bordin.2081387@studenti.uniroma1.it', ''),
           ( 'yes sir!', 'bordin.2081387@studenti.uniroma1.it', 'gabriele.lerani2000@gmail.com', ''),
           ( 'I cannot find the place we talked about', 'giralda.1903088@studenti.uniroma1.it', 'gabriele.lerani2000@gmail.com', ''),
           ( 'send me your cellphone number, I wil call you', 'gabriele.lerani2000@gmail.com',  'giralda.1903088@studenti.uniroma1.it', '');