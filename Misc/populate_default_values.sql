USE CS3205;
INSERT INTO admin VALUES (default, 'suser', '213c8fc7542d6d6f574153d293ad289dfad7df2a6df6f37f0291cce30fe9b58d', '$2y$10$QLPG5JlLudSu3CHo2xkCFO', '96bc7880544e66a202b24a58440487a6b7df131a');
INSERT INTO user VALUES (default, "Bob99", "27d7f741010bc4aa99eb9bb430a3922c1284a33f2bd833b628c66294a0c58fed", "","$2y$10$t9P.gFIa9nXhQSMaIbfOqu","", "Bobby", "Mike", "S1234567Z", DATE '2000-12-01', 
'M', 98989898, 97979797, 96969696, "Kent Ridge", "PGP", "Sentosa Cove", 555555, 544444, 533333, 0, "B+", "123", "somesecret", "Indian", "Singaporean", 1
);
INSERT INTO user VALUES (default, "Alan88", "27d7f741010bc4aa99eb9bb430a3922c1284a33f2bd833b628c66294a0c58fed", "","$2y$10$t9P.gFIa9nXhQSMaIbfOqu","", "Alan", "Homie", "S1231231B", DATE '1995-12-29', 
'M', 999888777, 999777666, 999666555, "Kent Ridge", "PGP", "Sentosa Cove", 543211, 5433333, 546666, 1, "A+", "1234455", "somesecret", "Chinese", "Malaysia", 0
);
INSERT INTO data VALUES (default, 1, "File", "image", "Image of user 1", " 2017-10-22 12:46:41", " 2017-10-22 12:46:41 ", "1507992509630_161UFavhLQy6cRUDZ2s1pH.jpeg");
INSERT INTO data VALUES (default, 1, "File", "video", "Galt Video of user 1", " 2017-10-22 12:46:41", " 2017-10-22 12:46:41 ", "vid1.mp4");

INSERT INTO treatment VALUES (default, 1, 2, 0, 1, 1);