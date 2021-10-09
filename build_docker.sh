version=$1

if [ -z $version ]
then
 echo "usage: $0 <version>"
 exit 1
fi

jarfile=target/solis-${version}.jar

imagename=daniu/solis:$version
docker build --build-arg JARFILE=${jarfile} -t $imagename -f docker/Dockerfile .
docker tag $imagename daniu/solis:${version}-armv7
