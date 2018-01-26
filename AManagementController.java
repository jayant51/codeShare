package <name>.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;

import <name>.<some>UsersBean;
import <name>.beans.<wspace>.SessionContainer;
import <name>.beans.UserInfo;
import <name>.beans.UserOfferInfo;
import <name>.constants.<wspace>.Constants;
import <name>.dao.AccountManagementDAO;

@RestController
@RequestMapping("/accountServices")
@SessionAttributes({<wspace>.Constants.<name>._CONTAINER_SESSION})
public class AManagementController extends <wspace>.Context {

    private static final Logger LOGGER = LoggerFactory.getLogger(AManagementController.class);

    @Autowired
    AccountManagementDAO accountManagementDAO;

    /**
     * API to list all logged in user account details with all offers
     * @param offerName
     * @return
     */
    @RequestMapping(value = "/listUserAccountDetails", method = RequestMethod.POST)
    public Map<String, Object> getUser<name>.Details(
            @ModelAttribute(<wspace>.Constants.<name>.CONTAINER_SESSION) <wspace>.SessionContainer sessionContainer) {
        Map<String, Object> accountsMap = new HashMap<String, Object>();
        UserInfo loggedInUser = null;
        try {
            if (sessionContainer != null && sessionContainer.getUserInfo() != null) {
                loggedInUser = sessionContainer.getUserInfo();
            } else {
                loggedInUser = new UserInfo();
                loggedInUser.setUserName("<guestuser>");
                loggedInUser.setFirstName("<guestuser>");
                loggedInUser.setLastName("<guestuser>");
                loggedInUser.setAdmin(false);
                loggedInUser.setUserId(7L);
            }

            Map<String, List<<some>UsersBean>> wrapperMap =
                    accountManagementDAO.get<some>Details(loggedInUser);

            accountsMap.put("wrapperMap", wrapperMap);

        } catch (Exception e) {
            LOGGER.error("Exception occurred while fetching user account details", e);
        }

        return accountsMap;
    }

    /**
     * API to list all logged in user account details with all offers
     * @param offerName
     * @return
     */
    @RequestMapping(value = "/addUserRecord", method = RequestMethod.POST)
    public Map<String, Object> addUserRecord(HttpSession session,
            @RequestBody UserOfferInfo userOfferInfo,
            @ModelAttribute(<wspace>.Constants.<name>.CONTAINER_SESSION) <wspace>.SessionContainer sessionContainer) {

        Map<String, Object> accountsMap = new HashMap<String, Object>();
        UserInfo loggedInUser = null;
        try {

            if (sessionContainer != null && sessionContainer.getUserInfo() != null) {
                loggedInUser = sessionContainer.getUserInfo();
            } else {
                loggedInUser = new UserInfo();
                loggedInUser.setUserName("<guestuser>");
                loggedInUser.setFirstName("<guestuser>");
                loggedInUser.setLastName("<guestuser>");
                loggedInUser.setAdmin(false);
                loggedInUser.setUserId(7L);
            }

            accountManagementDAO.addUserRecord(userOfferInfo, loggedInUser);

        } catch (Exception e) {
            LOGGER.error("Exception occurred while adding user record", e);
        }

        return accountsMap;
    }

    /**
     * addUserToOffer: API to add user to offer
     * @param offerName
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/addUserToOffer", method = RequestMethod.POST)
    public Map<String, List<String>> addUserToOffer(HttpSession session,
            @RequestBody UserOfferInfo userOfferInfo,
            @ModelAttribute(<wspace>.Constants.<name>.CONTAINER_SESSION) <wspace>.SessionContainer sessionContainer) {
        Map<String, List<String>> response = null;
        UserInfo loggedInUser = null;

        try {

            if (sessionContainer != null && sessionContainer.getUserInfo() != null) {
                loggedInUser = sessionContainer.getUserInfo();
            } else {
                loggedInUser = new UserInfo();
                loggedInUser.setUserName("<guestuser>");
                loggedInUser.setFirstName("<guestuser>");
                loggedInUser.setLastName("<guestuser>");
                loggedInUser.setAdmin(false);
                loggedInUser.setUserId(7L);
            }

            response = accountManagementDAO.addUserAccounttoOffer(userOfferInfo, loggedInUser);

        } catch (Exception e) {

            LOGGER.error("Exception occurred while adding user to offer source ", e);

        }

        return response;
    }

    /**
     * addUserToOffer: API to add user to offer
     * @param offerName
     * @return
     */

    @ResponseBody
    @RequestMapping(value = "/updateUser", method = RequestMethod.POST)
    public Map<String, Map<Integer, String>> updateUser(HttpSession session,
            @RequestBody Map<String, List<<some>UsersBean>> OfferUserMap,
            @ModelAttribute(<wspace>.Constants.<name>.CONTAINER_SESSION) <wspace>.SessionContainer sessionContainer) {

        Map<String, Map<Integer, String>> returnMessage =
                new HashMap<String, Map<Integer, String>>();
        UserInfo loggedInUser = null;

        try {

            if (sessionContainer != null && sessionContainer.getUserInfo() != null) {
                loggedInUser = sessionContainer.getUserInfo();
            } else {
                loggedInUser = new UserInfo();
                loggedInUser.setUserName("<guestuser>");
                loggedInUser.setFirstName("<guestuser>");
                loggedInUser.setLastName("<guestuser>");
                loggedInUser.setAdmin(false);
                loggedInUser.setUserId(7L);
            }
            returnMessage = accountManagementDAO.updateUser(
                    (Map<String, List<<some>UsersBean>>) OfferUserMap, loggedInUser);

        } catch (Exception e) {
            LOGGER.error("Exception occurred while fetching comments for source ", e);
        }

        return returnMessage;
    }

}
