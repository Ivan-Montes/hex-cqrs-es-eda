
let conn = new Mongo();
db = conn.getDB("mongo-read-db");

db.flights.createIndex({ flight_id: 1 }, {unique: true});
db.flights.insertOne({ flight_id:UUID('cb62f3ef-8c9f-4715-ad5c-f700201eac57'), flight_origin:"Vault101", flight_destiny:"Vault33", flight_departure_date:ISODate('2000-11-09T22:00:00.000Z'), flight_departure_time:ISODate('2024-06-03T13:31:00.000Z'), plane_id:UUID('538c58eb-9ac3-4bef-9b37-a720e6faf00d'), _class: 'dev.ime.infrastructure.entity.FlightMongoEntity'} );
db.flights.insertOne({ flight_id:UUID('cb62f3ef-8c9f-4715-ad5c-f700201eac58'), flight_origin:"Krakoa Island", flight_destiny:"Genosha", flight_departure_date:ISODate('2000-11-11T22:00:00.000Z'), flight_departure_time:ISODate('2024-06-03T13:32:00.000Z'), plane_id:UUID('538c58eb-9ac3-4bef-9b37-a720e6faf00e'), _class: 'dev.ime.infrastructure.entity.FlightMongoEntity'} );
db.flights.insertOne({ flight_id:UUID('cb62f3ef-8c9f-4715-ad5c-f700201eac59'), flight_origin:"Bermuda Triangle", flight_destiny:"Antarctica base", flight_departure_date:ISODate('2000-11-13T22:00:00.000Z'), flight_departure_time:ISODate('2024-06-03T13:33:00.000Z'), plane_id:UUID('538c58eb-9ac3-4bef-9b37-a720e6faf00d'), _class: 'dev.ime.infrastructure.entity.FlightMongoEntity'} );