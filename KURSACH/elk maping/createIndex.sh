curl -X PUT "localhost:9200/test5" -H 'Content-Type: application/json' -d'
{
    "settings" : {
        "index" : {
            "number_of_shards" : 3, 
            "number_of_replicas" : 1 
        }
    }
}
'
