
let conn = new Mongo();
db = conn.getDB("mongo-read-db");

db.clients.createIndex({ client_id: 1 }, {unique: true});
db.clients.insertOne({ client_id:UUID('f7b40b74-36ed-434a-ae2a-394f6c7e2f51'), client_name:"Peter", client_lastname:"Pork", _class: 'dev.ime.infrastructure.entity.ClientMongoEntity'});
db.clients.insertOne({ client_id:UUID('f7b40b74-36ed-434a-ae2a-394f6c7e2f52'), client_name:"Gwen", client_lastname:"Stacy", _class: 'dev.ime.infrastructure.entity.ClientMongoEntity'});
db.clients.insertOne({ client_id:UUID('f7b40b74-36ed-434a-ae2a-394f6c7e2f53'), client_name:"Mary Jane", client_lastname:"Watson", _class: 'dev.ime.infrastructure.entity.ClientMongoEntity'});
db.clients.insertOne({ client_id:UUID('f7b40b74-36ed-434a-ae2a-394f6c7e2f54'), client_name:"Miles", client_lastname:"Morales", _class: 'dev.ime.infrastructure.entity.ClientMongoEntity'});
db.clients.insertOne({ client_id:UUID('f7b40b74-36ed-434a-ae2a-394f6c7e2f55'), client_name:"Norman", client_lastname:"Osborn", _class: 'dev.ime.infrastructure.entity.ClientMongoEntity'});
db.clients.insertOne({ client_id:UUID('f7b40b74-36ed-434a-ae2a-394f6c7e2f56'), client_name:"Felicia", client_lastname:"Hardy", _class: 'dev.ime.infrastructure.entity.ClientMongoEntity'});
