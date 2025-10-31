# syntax=docker/dockerfile:experimental
FROM gradle:8.0.2-jdk17-alpine AS build
WORKDIR /workspace/app

COPY . /workspace/app
RUN apk add gcompat
RUN chmod +x ./gradlew
RUN --mount=type=cache,target=/.gradle ./gradlew clean build -x test
RUN mkdir -p build/dependency && (cd build/dependency; jar -xf ../libs/*-SNAPSHOT.jar)

FROM gradle:8.0.2-jdk17-alpine
VOLUME /tmp
ARG DEPENDENCY=/workspace/app/build/dependency
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app
WORKDIR /
ENTRYPOINT ["java","-cp","app:app/lib/*","ru.ufanet.pool_api.PoolApiApplication"]