bin/solr start -d server;
bin/solr create -c verdict;
cp /managed-schema /opt/solr/server/solr/verdict/conf/managed-schema;
bin/solr restart;