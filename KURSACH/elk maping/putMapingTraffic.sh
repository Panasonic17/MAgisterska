curl -X PUT "http://127.0.0.1:9200/test/_mapping/test"  -H 'Content-Type: application/json' -d'
{
         "te123":{  
            "properties":{  
               "aircraft":{  
                  "type":"text",
                  "fields":{  
                     "keyword":{  
                        "type":"keyword",
                        "ignore_above":256
                     }
                  }
               },
               "altitude":{  
                  "type":"text",
                  "fields":{  
                     "keyword":{  
                        "type":"keyword",
                        "ignore_above":256
                     }
                  }
               },
               "callsign":{  
                  "type":"text",
                  "fields":{  
                     "keyword":{  
                        "type":"keyword",
                        "ignore_above":256
                     }
                  }
               },
               "course":{  
                  "type":"long"
               },
               "destination":{  
                  "type":"text",
                  "fields":{  
                     "keyword":{  
                        "type":"keyword",
                        "ignore_above":256
                     }
                  }
               },
                "destinationCity":{  
                      "type":"text",
                       "fields":{  
                           "keyword":{  
                           "type":"keyword",
                           "ignore_above":256
                            }
                        }
                    },
                    "destinationCountry":{  
                        "type":"text",
                        "fields":{  
                            "keyword":{  
                            "type":"keyword",
                            "ignore_above":256
                            }
                        }
                    },
               "flight":{  
                  "type":"text",
                  "fields":{  
                     "keyword":{  
                        "type":"keyword",
                        "ignore_above":256
                     }
                  }
               },
               "lat":{  
                  "type":"float"
               },
               "lon":{  
                  "type":"float"
               },
               "origin":{  
                  "type":"text",
                  "fields":{  
                     "keyword":{  
                        "type":"keyword",
                        "ignore_above":256
                     }
                  }
               },
                "originCity":{  
                     "type":"text",
                        "fields":{  
                            "keyword":{  
                            "type":"keyword",
                            "ignore_above":256
                            }
                        }
                    },
                "originCountry":{  
                    "type":"text",
                    "fields":{  
                        "keyword":{  
                        "type":"keyword",
                        "ignore_above":256
                        }
                    }
                },
               "registration":{  
                  "type":"text",
                  "fields":{  
                     "keyword":{  
                        "type":"keyword",
                        "ignore_above":256
                     }
                  }
               },
               "speed":{  
                  "type":"long"
               },
               "time":{  
                  "type":"date",
                  "format": "epoch_millis"
               }
            }
         }
      }
   }'