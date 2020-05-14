package com.tensquare.spit.comtroller;

import com.tensquare.spit.po.Spit;
import com.tensquare.spit.service.SpitService;
import constants.StatusCode;
import dto.PageResultDTO;
import dto.ResultDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

/**
 *
 */
@RestController
@CrossOrigin
@RequestMapping("/spit")
public class SpitController {

    @Autowired
    private SpitService spitService;
    /**
     * 增加
     * @param spit
     */
    @PostMapping
    public ResultDTO add(@RequestBody Spit spit){
        spitService.saveSpit(spit);
        return new ResultDTO(true, StatusCode.OK,"增加成功");
    }

    /**
     * 修改
     * @param spit
     */
    @PutMapping("/{id}")
    public ResultDTO edit(@RequestBody Spit spit, @PathVariable String id ){
        spit.setId(id);
        spitService.updateSpit(spit);
        return new ResultDTO(true,StatusCode.OK,"修改成功");
    }

    /**
     * 删除
     * @param id
     */
    @DeleteMapping("/{id}")
    public ResultDTO remove(@PathVariable String id ){
        spitService.deleteSpitById(id);
        return new ResultDTO(true,StatusCode.OK,"删除成功");
    }

    /**
     * 查询全部数据
     * @return
     */
    @GetMapping
    public ResultDTO list(){
        return new ResultDTO(true,StatusCode.OK,"查询成功",spitService.findSpitList());
    }

    /**
     * 根据ID查询
     * @param id ID
     * @return
     */
    @GetMapping("/{id}")
    public ResultDTO listById(@PathVariable String id){
        return new ResultDTO(true,StatusCode.OK,"查询成功",spitService.findSpitById(id));
    }
    /**
     * 根据上级ID查询吐槽分页数据
     * @param parentid
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/comment/{parentid}/{page}/{size}")
    public ResultDTO listPageByParentid(@PathVariable String parentid , @PathVariable int page, @PathVariable int size){
        Page<Spit> pageResponse = spitService.findSpitListPageByParentid(parentid, page, size);
        return  new ResultDTO(true,StatusCode.OK,"查询成功",  new PageResultDTO<Spit>(pageResponse.getTotalElements(), pageResponse.getContent()) );
    }
    /**
     *  根据吐槽id，增加点赞的数量
     * @param id  效率低
     * @return
     */
    @PutMapping("/thumbup/{id}")
    public ResultDTO incrementThumbup(@PathVariable String id){
        spitService.updateSpitThumbupToIncrementingOld(id);
        return new ResultDTO(true,StatusCode.OK,"点赞成功");
    }
    /**
     *  根据吐槽id，增加点赞的数量
     * @param id
     * @return
     */
    @Autowired
    private StringRedisTemplate redisTemplate;
    @PutMapping("/thumbup/inc/{id}")
    public ResultDTO incrementThumbupInc(@PathVariable String id){
        //目标：某用户24小时内只能点赞一次
        //1)获取当前登录的用户，后边我们会修改为真正的当前登陆的用户（先写死）
        String userid="2023";
        //2)定义已经点赞的用户在Redis中存放的key
        String redisKey="thumbup_"+userid+"_"+ id;
        //3)判断用户是否点过赞
        if(null!=redisTemplate.opsForValue().get(redisKey)){
            //如果存在，说明该用户已经点过赞了
            return new ResultDTO(false, StatusCode.REPERROR,"您已经点过赞了");
        }
        //24小时内没有点过赞的
        //点赞
        spitService.updateSpitThumbupToIncrementing(id);
        //在redis中记录一下:将该用户和吐槽信息放入Redis缓存,并设置24小时内有效
        //redisTemplate.opsForValue().set(redisKey,"1",1, TimeUnit.DAYS);
        redisTemplate.opsForValue().set(redisKey,"1",15, TimeUnit.SECONDS);
        return new ResultDTO(true,StatusCode.OK,"点赞成功");
    }
}
