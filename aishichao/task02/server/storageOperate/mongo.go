package storageOperate

import (
	"context"
	"go.mongodb.org/mongo-driver/bson"
	"go.mongodb.org/mongo-driver/mongo"
	"go.mongodb.org/mongo-driver/mongo/options"
	"log"
	"server/serverDataType"
	"time"
)

var MongoDatabase *mongo.Collection

func InitMongo() {
	clientOption := options.Client().ApplyURI("mongodb://mongo:27017")
	client, err := mongo.Connect(context.TODO(), clientOption)
	if err != nil {
		log.Println("connect to mongo error:", err)
		return
	}
	MongoDatabase = client.Database("mongoDB").Collection("mongoCollection")
}

func InsertLogsIntoMongo(logs serverDataType.LogInformation) error {
	if logs.Logs == nil {
		return nil
	}
	ctx, cancel := context.WithTimeout(context.Background(), 5*time.Second)
	defer cancel()
	_, err := MongoDatabase.InsertOne(ctx, logs)
	return err
}

func QueryLogsFromMongo(hostname, file string) ([]serverDataType.LogInformation, error) {
	var filter bson.M
	if hostname == "" && file == "" {
		filter = bson.M{}
	} else if hostname != "" && file == "" {
		filter = bson.M{"hostname": hostname}
	} else if hostname == "" {
		filter = bson.M{"file": file}
	} else {
		filter = bson.M{"hostname": hostname, "file": file}
	}

	ctx, cancel := context.WithTimeout(context.Background(), 10*time.Second)
	defer cancel()

	cursor, err := MongoDatabase.Find(ctx, filter, options.Find())
	if err != nil {
		log.Printf("Error querying MongoDB: %v", err)
		return nil, err
	}
	defer func(cursor *mongo.Cursor, ctx context.Context) {
		err := cursor.Close(ctx)
		if err != nil {
			log.Printf("Error close MongoDB: %v", err)
		}
	}(cursor, ctx)

	var logs []serverDataType.LogInformation
	for cursor.Next(ctx) {
		var logInfo serverDataType.LogInformation
		if err := cursor.Decode(&logInfo); err != nil {
			log.Printf("Error decoding MongoDB document: %v", err)
			continue
		}
		logs = append(logs, logInfo)
	}

	if err := cursor.Err(); err != nil {
		log.Printf("Error during cursor iteration: %v", err)
		return nil, err
	}

	return logs, nil
}
