package com.community.controller;

import com.community.common.Result;
import com.community.entity.Community;
import com.community.service.CommunityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/community")
public class CommunityController {

    @Autowired
    private CommunityService communityService;

    @GetMapping("/info")
    public Result<Community> getInfo() {
        return Result.success(communityService.getCommunityInfo());
    }

    @PutMapping("/update")
    public Result<String> update(@RequestBody Community community) {
        boolean success = communityService.updateCommunityInfo(community);
        return success ? Result.success("更新成功") : Result.error("更新失败");
    }
}
