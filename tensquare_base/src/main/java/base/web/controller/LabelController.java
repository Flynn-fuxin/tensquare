package base.web.controller;


import base.po.Label;
import base.service.LabelService;
import constants.StatusCode;
import dto.PageResultDTO;
import dto.ResultDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 标签控制层
 */
@RestController
@RequestMapping("/label")
@CrossOrigin(
        origins = "http://anotherdomain.com",
        maxAge = 3600,
        methods = {RequestMethod.GET, RequestMethod.POST})
public class LabelController {
    @Autowired
    private LabelService labelService;

    /**
     * 添加一个
     * @param label
     * @return
     */
    @PostMapping
    public ResultDTO add(@RequestBody Label label){
        labelService.saveLabel(label);
        return new ResultDTO(true, constants.StatusCode.OK,"添加成功");
    }

    /**
     * 修改编辑
     * @param label
     * @param id
     * @return
     */
    @PutMapping("/{id}")
    public ResultDTO edit(@RequestBody Label label, @PathVariable String id){
        label.setId(id);
        labelService.updateLabel(label);
        return new ResultDTO(true, constants.StatusCode.OK,"修改成功");
    }

    /**
     * 根据id删除一个
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ResultDTO remove(@PathVariable String id){
        labelService.deleteLabelById(id);
        return new ResultDTO(true, constants.StatusCode.OK,"删除成功");
    }
    
    
    /**
     * 查询所有
     * @return
     */
    @GetMapping
    public ResultDTO list(){
        List<Label> list = labelService.findLabelList();
        return new ResultDTO(true, constants.StatusCode.OK,"查询成功",list);
    }

    @Autowired
    private HttpServletRequest request;
    /**
     * 根据id查询一个
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResultDTO listById(@PathVariable String id){
        System.out.println("当前服务端口："+ request.getServerPort());
        Label label = labelService.findLabelById(id);
        return new ResultDTO(true, constants.StatusCode.OK,"查询成功",label);
    }
    /**
     * 根据条件查
     * @return
     */
    @PostMapping("/search")
    public ResultDTO search(@RequestBody Map<String,Object> map){
        List<Label> list = labelService.findLabelList(map);
        return new ResultDTO(true, constants.StatusCode.OK,"查询成功",list);
    }

    /**
     * 组合条件分页查询列表
     * @param map
     * @param page
     * @param size
     * @return
     */
    @PostMapping("/search/{page}/{size}")
    public ResultDTO list(@RequestBody Map<String,Object> map,@PathVariable int page, @PathVariable int size){
        Page<Label> list = labelService.findLabelList(map,page,size);
        PageResultDTO pageResultDTO = new PageResultDTO(list.getTotalElements(),list.getContent());
        return new ResultDTO(true, StatusCode.OK,"查询成功",pageResultDTO);
    }

}
