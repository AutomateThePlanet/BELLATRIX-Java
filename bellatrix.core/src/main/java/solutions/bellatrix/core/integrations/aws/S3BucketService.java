///*
// * Copyright 2022 Automate The Planet Ltd.
// * Author: Anton Angelov
// * Licensed under the Apache License, Version 2.0 (the "License");
// * You may not use this file except in compliance with the License.
// * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//package solutions.bellatrix.core.integrations.aws;
//
//import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
//import software.amazon.awssdk.core.sync.RequestBody;
//import software.amazon.awssdk.core.waiters.WaiterResponse;
//import software.amazon.awssdk.regions.Region;
//import software.amazon.awssdk.services.s3.S3Client;
//import software.amazon.awssdk.services.s3.model.*;
//import software.amazon.awssdk.services.s3.waiters.S3Waiter;
//
//import java.io.File;
//import java.nio.file.Path;
//import java.util.List;
//import java.util.stream.Collectors;
//
//public class S3BucketService {
//    public void downloadFile(Region region, String bucketName, String key, String resultFilePath) {
//        S3Client client = S3Client.builder()
//                .region(region)
//                .credentialsProvider(ProfileCredentialsProvider.create())
//                .build();
//        try(client) {
//            var getRequest = GetObjectRequest.builder().bucket(bucketName).key(key).build();
//
//            // https://www.baeldung.com/aws-s3-java
//            client.getObject(getRequest, Path.of(resultFilePath));
//        }
//    }
//
//    public void uploadFile(Region region, String bucketName, String sourceFilePath, String resultFileName) {
//        S3Client client = S3Client.builder()
//                .region(region)
//                .credentialsProvider(ProfileCredentialsProvider.create())
//                .build();
//        try(client) {
//            PutObjectRequest request = PutObjectRequest.builder()
//                    .bucket(bucketName).key(resultFileName).build();
//
//            client.putObject(request, RequestBody.fromFile(new File(sourceFilePath)));
//
//            // wait to be uploaded
//            S3Waiter waiter = client.waiter();
//            HeadObjectRequest requestWait = HeadObjectRequest.builder().bucket(bucketName).key(resultFileName).build();
//
//            WaiterResponse<HeadObjectResponse> waiterResponse = waiter.waitUntilObjectExists(requestWait);
//
//            waiterResponse.matched().response().ifPresent(System.out::println);
//        }
//
//    }
//
//    public void uploadFile(Region region, String bucketName, byte[] data, String resultFileName) {
//        S3Client client = S3Client.builder()
//                .region(region)
//                .credentialsProvider(ProfileCredentialsProvider.create())
//                .build();
//        try(client) {
//            PutObjectRequest request = PutObjectRequest.builder()
//                    .bucket(bucketName).key(resultFileName).build();
//
//            client.putObject(request, RequestBody.fromBytes(data));
//
//            // wait to be uploaded
//            S3Waiter waiter = client.waiter();
//            HeadObjectRequest requestWait = HeadObjectRequest.builder().bucket(bucketName).key(resultFileName).build();
//
//            WaiterResponse<HeadObjectResponse> waiterResponse = waiter.waitUntilObjectExists(requestWait);
//
//            waiterResponse.matched().response().ifPresent(System.out::println);
//        }
//
//    }
//
//    public void deleteFile(Region region, String bucketName, String sourceFilePath) {
//        S3Client client = S3Client.builder()
//                .region(region)
//                .credentialsProvider(ProfileCredentialsProvider.create())
//                .build();
//        try(client) {
//            DeleteObjectRequest request = DeleteObjectRequest.builder()
//                    .bucket(bucketName)
//                    .key(sourceFilePath)
//                    .build();
//
//            client.deleteObject(request);
//        }
//    }
//
//    public void deleteMultipleFiles(Region region, String bucketName, List<String> filesToBeDeleted) {
//        S3Client client = S3Client.builder()
//                .region(region)
//                .credentialsProvider(ProfileCredentialsProvider.create())
//                .build();
//        try(client) {
//            List<ObjectIdentifier> listObjects =
//                    filesToBeDeleted.stream().map(f -> ObjectIdentifier.builder().key(f).build()).collect(Collectors.toList());
//
//            DeleteObjectsRequest request = DeleteObjectsRequest.builder()
//                    .bucket(bucketName)
//                    .delete(Delete.builder().objects(listObjects).build())
//                    .build();
//
//            DeleteObjectsResponse response = client.deleteObjects(request);
//        }
//    }
//
//    private void waitForFileToBeUploaded(String bucketName, Region region, String resultFileName) {
//        S3Client client = S3Client.builder()
//                .region(region)
//                .credentialsProvider(ProfileCredentialsProvider.create())
//                .build();
//        try(client) {
//            S3Waiter waiter = client.waiter();
//            HeadObjectRequest requestWait = HeadObjectRequest.builder().bucket(bucketName).key(resultFileName).build();
//
//            WaiterResponse<HeadObjectResponse> waiterResponse = waiter.waitUntilObjectExists(requestWait);
//
//            waiterResponse.matched().response().ifPresent(System.out::println);
//        }
//    }
//}
