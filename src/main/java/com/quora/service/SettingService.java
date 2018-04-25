package com.quora.service;

import org.springframework.stereotype.Service;

/**
 * description here
 *
 * @author: leon
 * @date: 2018/4/25 19:40
 * @version: 1.0
 */
@Service
public class SettingService {

    public String getId(int id) {
        return "id: " + id;
    }
}
