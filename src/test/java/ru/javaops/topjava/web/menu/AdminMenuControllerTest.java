package ru.javaops.topjava.web.menu;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.javaops.topjava.error.IllegalRequestDataException;
import ru.javaops.topjava.repository.MenuRepository;
import ru.javaops.topjava.to.MenuTo;
import ru.javaops.topjava.util.JsonUtil;
import ru.javaops.topjava.web.AbstractControllerTest;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javaops.topjava.util.MenuUtil.convertFromMenu;
import static ru.javaops.topjava.web.menu.AdminMenuTestData.*;
import static ru.javaops.topjava.web.restaurant.RestaurantTestData.RESTAURANT1_ID;
import static ru.javaops.topjava.web.restaurant.RestaurantTestData.WRONG_RESTAURANT_ID;
import static ru.javaops.topjava.web.user.UserTestData.ADMIN_MAIL;

class AdminMenuControllerTest extends AbstractControllerTest {

    private final String REST_URL = "/api/admin/restaurants/";
    private final String MENU = "/menu/";
    private static final String EXCEPTION_DUPLICATE_MENU_DATE_NAME = "Menu in this date with this name already exists";


    @Autowired
    private MenuRepository menuRepository;

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getAllByRestaurantId() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + RESTAURANT1_ID + MENU))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MENU_MATCHER.contentJson(menu3, menu4, menu1, menu2));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + RESTAURANT1_ID + MENU + MENU1_ID))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MENU_MATCHER.contentJson(menu1));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getAllByDate() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + MENU))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MENU_TO_MATCHER.contentJson(todayMenu));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getWithWrongRestaurantId() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + RESTAURANT1_ID + MENU + MENU5_ID))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof IllegalRequestDataException))
                .andExpect(result -> assertEquals(MenuController.MENU_NOT_FOUND,
                        Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void create() throws Exception {
        MenuTo newMenuTo = AdminMenuTestData.getNew();
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL + RESTAURANT1_ID + MENU)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newMenuTo)))
                .andExpect(status().isCreated());

        MenuTo createdTo = MENU_TO_MATCHER.readFromJson(action);
        int newId = createdTo.id();
        newMenuTo.setId(newId);
        MenuTo savedMenuTo = convertFromMenu(menuRepository.getById(newId));
        MenuTo expected = getNewAfterSaveInRepo();
        expected.setId(savedMenuTo.getId());
        MENU_TO_MATCHER.assertMatch(createdTo, newMenuTo);
        MENU_TO_MATCHER.assertMatch(savedMenuTo, expected);
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void createWithWrongDataForMenuTo() throws Exception {
        MenuTo newMenuTo = AdminMenuTestData.getNewWithWrongData();
        perform(MockMvcRequestBuilders.post(REST_URL + RESTAURANT1_ID + MENU)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newMenuTo)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void createWithWrongRestaurantId() throws Exception {
        MenuTo newMenuTo = AdminMenuTestData.getNew();
        perform(MockMvcRequestBuilders.post(REST_URL + WRONG_RESTAURANT_ID + MENU)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newMenuTo)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void createWithNotUniqueDateAndName() throws Exception {
        perform(MockMvcRequestBuilders.post(REST_URL + RESTAURANT1_ID + MENU)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(getWithNotUniqueDateAndName())))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().string(Matchers.containsString(EXCEPTION_DUPLICATE_MENU_DATE_NAME)));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void update() throws Exception {
        MenuTo updatedMenuTo = AdminMenuTestData.getNew();
        perform(MockMvcRequestBuilders.put(REST_URL + RESTAURANT1_ID + MENU + MENU1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updatedMenuTo)))
                .andDo(print())
                .andExpect(status().isNoContent());

        updatedMenuTo.setId(MENU1_ID);
        MENU_TO_MATCHER.assertMatch(convertFromMenu(menuRepository.getById(MENU1_ID)), updatedMenuTo);
        MENU_TO_MATCHER.assertMatch(
                convertFromMenu(menuRepository.getById(MENU1_ID)), convertFromMenu(AdminMenuTestData.getUpdated()));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void updateWithWrongDataForMenuTo() throws Exception {
        MenuTo updatedMenuTo = AdminMenuTestData.getNewWithWrongData();
        perform(MockMvcRequestBuilders.put(REST_URL + RESTAURANT1_ID + MENU + MENU1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updatedMenuTo)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
        MENU_TO_MATCHER.assertMatch(convertFromMenu(menuRepository.getById(MENU1_ID)), convertFromMenu(menu1));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + RESTAURANT1_ID + MENU + MENU1_ID))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertFalse(menuRepository.findById(MENU1_ID).isPresent());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void deleteWithWrongId() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + RESTAURANT1_ID + MENU + MENU5_ID))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
        assertTrue(menuRepository.findById(MENU5_ID).isPresent());
    }
}
