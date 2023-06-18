/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tendencias.app.Usuarios.controller;

import com.tendencias.app.Usuarios.model.vm.Asset;
import com.tendencias.app.Usuarios.service.S3service;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author lilis
 */
@RestController
@RequestMapping("/asset")
public class AssetController {
     @Autowired
    private S3service s3Service;
    
    @PostMapping("/upload")
    Map<String , String> upload(@RequestParam MultipartFile file){
        String Key = s3Service.putObject(file);
        
        Map<String , String> result = new HashMap<>();
        result.put("key", Key);
        result.put("url", s3Service.getObjectUrl(Key));
        
        return result;
    }
     @GetMapping(value="/get-object" , params= "Key")
    public ResponseEntity<ByteArrayResource> getObject(@RequestParam String key) {
       Asset asset =s3Service.getObject(key);
       ByteArrayResource resource = new ByteArrayResource(asset.getContent());
       
       return ResponseEntity
               .ok()
               .header("Content-Type", asset.getContentType())
               .contentLength(asset.getContent().length)
               .body(resource);
    }
    @DeleteMapping(value="/delete-object", params="key")
    void deleteObject(@RequestParam String key){
        s3Service.deletObject(key);
        
    }
}
