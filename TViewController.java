package <name>.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;

import <name>.beans.FilterBean;
import <name>.beans.<some>Bean;
import <name>.beans.<space>SessionContainer;
import <name>.beans.TaskViewCountBean;
import <name>.beans.UserInfo;
import <name>.constants.<space>Constants;
import <name>.dao.CountDAO;
import <name>.dao.DashboardDAO;

@RestController
@RequestMapping("/taskView")
@SessionAttributes({<space>Constants.<name>.CONTAINER_SESSION})
public class TViewController extends <space>Context {
   
    private static final Logger LOGGER = LoggerFactory.getLogger(TViewController.class);

    @Autowired
    CountDAO countDAO;

    @Autowired
    DashboardDAO dashboardDAO;

    /**
     * API that returns the dashboard data
     * Data returned is a collection of <some> records along with the PIs defined under it
     * Data is filter as per the options defined by the attributes of the Filter bean
     * This includes the pagination number and size that defined the size of the data returned in the response
     * Note: <space> has implemented a server side pagination to enhance speed as well as reduce the size of the response payload
     * @param session
     * @param filter
     * @param sessionContainer
     * @return
     */
    @RequestMapping(value = "/taskList", method = RequestMethod.POST)
    public Object getTaskList(HttpSession session, @RequestBody FilterBean filter,
            @ModelAttribute(<space>Constants.<name>.CONTAINER_SESSION) <space>SessionContainer sessionContainer) {
        Map<String, Object> taskListMap = null;
        try {
            if (sessionContainer != null && sessionContainer.getUserInfo() != null) {
                // filter dummy data based on <some> assigned to user
                UserInfo loggedInUser = sessionContainer.getUserInfo();
                taskListMap = dashboardDAO.get<some>DetailsAnd<some>ingInfo(loggedInUser, filter, true);
            } else {
                return new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            LOGGER.error("Exception Occured while fetching taskList ", e);
        }

        return taskListMap;
    }

    
    @RequestMapping(value = "/typeAheadList", method = RequestMethod.POST)
    public Object get<some>List(HttpSession session, @RequestBody FilterBean filter,
            @ModelAttribute(<space>Constants.<name>.CONTAINER_SESSION) <space>SessionContainer sessionContainer) {
    	Map<String, Object>  typeAheadLists = null;
        try {
            if (sessionContainer != null && sessionContainer.getUserInfo() != null) {
                UserInfo loggedInUser = sessionContainer.getUserInfo();
                typeAheadLists = dashboardDAO.getTypeAheadList(loggedInUser);
            } else {
                return new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            LOGGER.error("Exception Occured while fetching taskList ", e);
        }

        return typeAheadLists;
    }
    
    
    /**
     * API that returns the data used to paint the charts in the dashboard page
     * This pertains to two different types
     * 1. <some> Line Status Counts
     * 2. <some>ing Interface Status Counts
     * @param container
     * @return
     */
    @RequestMapping(value = "/taskViewCount", method = RequestMethod.POST)
    public Map<String, List<TaskViewCountBean>> getTaskViewCount(
            @ModelAttribute(<space>Constants.<name>.CONTAINER_SESSION) <space>SessionContainer container) {
        Map<String, List<TaskViewCountBean>> taskViewCountMap = null;
        try {
            if (container != null && container.getUserInfo() != null) {
                UserInfo loggedInUser = container.getUserInfo();
                taskViewCountMap = new HashMap<String, List<TaskViewCountBean>>();
                taskViewCountMap.put("<some>TypesList", countDAO.getStatusCount("<some>sInProgress", loggedInUser));
                taskViewCountMap.put("<some>ingGroupTypesList", countDAO.getStatusCount("<some>ingGroups", loggedInUser));
            }
        } catch (Exception e) {
            LOGGER.error("Exception Occured while fetching task count ", e);
        }
        return taskViewCountMap;
    }
    
    /**
     * API that returns the data used to paint to the major line detail page
     * @param session
     * @param requestObj
     * @param container
     * @return
     */
    @RequestMapping(value = "/majorLineDetail", method = RequestMethod.POST)
    public Object getMajorLineDetail(HttpSession session, @RequestBody Map<String, String> requestObj,
            @ModelAttribute(<space>Constants.<name>.CONTAINER_SESSION) <space>SessionContainer container) {
        <some>Bean <some> = null;
        String detailId = requestObj.get("detailId");
        String detailType = requestObj.get("detailType");
        String previous<some><some>Flag = requestObj.get("previous<some>DetailFlag");
        Boolean prvServ<some>Flag = false;

        if (<space>Constants.TRUE.equalsIgnoreCase(previous<some><some>Flag)){
            prvServ<some>Flag = true;
        }

        if (StringUtils.isNumeric(detailId)) {
            try {
                if (container != null && container.getUserInfo() != null) {
                    UserInfo loggedInUser = container.getUserInfo();
                    List<<some>Bean> <some>BeanList = null;
                    // filter data based on <some> assigned to user
                    Map<String, Object> data = dashboardDAO.get<some>DetailsForId(loggedInUser, Long.valueOf(detailId), detailType, null,
                            prvServ<some>Flag, false);
                    <some>BeanList = (List<<some>Bean>) data.get(<space>Constants.TASK_LIST);
                    if (<some>BeanList != null && !<some>BeanList.isEmpty()) {
                        <some> = <some>BeanList.get(0);
                    }
                }
            } catch (Exception e) {
                LOGGER.error("Exception Occured while fetching Major Line Detail ", e);
            }
            return <some>;
        } else {
            return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/checkrest", method = RequestMethod.POST)
    public void checkRest(@RequestBody String test) {
        if (test != null && test.indexOf("+") == -1) {
            dashboardDAO.insert(test);
        }
    }

}
