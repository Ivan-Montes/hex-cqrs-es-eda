
let conn = new Mongo();
db = conn.getDB("mongo-read-db");

db.seats.createIndex({ seat_id: 1 }, {unique: true});
db.seats.createIndex({ plane_id: 1 });
db.seats.insertOne({ seat_id:UUID('e0f4a724-9cda-4ec5-8ccb-734cb9e3b600'), seat_number:"QAR103", plane_id: UUID('538c58eb-9ac3-4bef-9b37-a720e6faf00d'), _class: 'dev.ime.infrastructure.entity.SeatMongoEntity'} );
db.seats.insertOne({ seat_id:UUID('e0f4a724-9cda-4ec5-8ccb-734cb9e3b601'), seat_number:"QAR084", plane_id: UUID('538c58eb-9ac3-4bef-9b37-a720e6faf00d'), _class: 'dev.ime.infrastructure.entity.SeatMongoEntity'} );
db.seats.insertOne({ seat_id:UUID('e0f4a724-9cda-4ec5-8ccb-734cb9e3b602'), seat_number:"QAR011", plane_id: UUID('538c58eb-9ac3-4bef-9b37-a720e6faf00d'), _class: 'dev.ime.infrastructure.entity.SeatMongoEntity'} );
db.seats.insertOne({ seat_id:UUID('e0f4a724-9cda-4ec5-8ccb-734cb9e3b603'), seat_number:"QAR063", plane_id: UUID('538c58eb-9ac3-4bef-9b37-a720e6faf00d'), _class: 'dev.ime.infrastructure.entity.SeatMongoEntity'} );
db.seats.insertOne({ seat_id:UUID('e0f4a724-9cda-4ec5-8ccb-734cb9e3b604'), seat_number:"BP128", plane_id: UUID('538c58eb-9ac3-4bef-9b37-a720e6faf00e'), _class: 'dev.ime.infrastructure.entity.SeatMongoEntity'} );
db.seats.insertOne({ seat_id:UUID('e0f4a724-9cda-4ec5-8ccb-734cb9e3b605'), seat_number:"BP061", plane_id: UUID('538c58eb-9ac3-4bef-9b37-a720e6faf00e'), _class: 'dev.ime.infrastructure.entity.SeatMongoEntity'} );
db.seats.insertOne({ seat_id:UUID('e0f4a724-9cda-4ec5-8ccb-734cb9e3b606'), seat_number:"BP035", plane_id: UUID('538c58eb-9ac3-4bef-9b37-a720e6faf00e'), _class: 'dev.ime.infrastructure.entity.SeatMongoEntity'} );

db.planes.createIndex({ plane_id: 1 }, {unique: true});
db.planes.insertOne( { plane_id:UUID('538c58eb-9ac3-4bef-9b37-a720e6faf00d'), plane_name:"Queen Anne Revenge", plane_capacity: 49, _class: 'dev.ime.infrastructure.entity.PlaneMongoEntity'});
db.planes.insertOne( { plane_id:UUID('538c58eb-9ac3-4bef-9b37-a720e6faf00e'), plane_name:"Black Pearl", plane_capacity: 77, _class: 'dev.ime.infrastructure.entity.PlaneMongoEntity'});
db.planes.insertOne( { plane_id:UUID('538c58eb-9ac3-4bef-9b37-a720e6faf00f'), plane_name:"Flying Dutchman", plane_capacity: 79, _class: 'dev.ime.infrastructure.entity.PlaneMongoEntity'});
