package com.lambdaschool.bookstore.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lambdaschool.bookstore.BookstoreApplication;
import com.lambdaschool.bookstore.models.*;
import com.lambdaschool.bookstore.services.BookService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)

/*****
 * Due to security being in place, we have to switch out WebMvcTest for SpringBootTest
 * @WebMvcTest(value = BookController.class)
 */
@SpringBootTest(classes = BookstoreApplication.class)

/****
 * This is the user and roles we will use to test!
 */
@WithMockUser(username = "admin", roles = {"ADMIN", "DATA"})
public class BookControllerTest
{
    /******
     * WebApplicationContext is needed due to security being in place.
     */
    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    List<Book> bookList = new ArrayList<>();

    @Before
    public void setUp() throws
            Exception
    {
        /*****
         * The following is needed due to security being in place!
         */
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();

        /*****
         * Note that since we are only testing bookstore data, you only need to mock up bookstore data.
         * You do NOT need to mock up user data. You can. It is not wrong, just extra work.
         */

        Role r1 = new Role("admin");
        Role r2 = new Role("user");
        Role r3 = new Role("data");

        r1.setRoleid(1);
        r2.setRoleid(2);
        r3.setRoleid(3);
//        r1 = roleService.save(r1);
//        r2 = roleService.save(r2);
//        r3 = roleService.save(r3);

        // admin, data, user
        User u1 = new User("test admin",
                "password",
                "admin@lambdaschool.local");
        u1.setUserid(10);

        u1.getRoles()
                .add(new UserRoles(u1, r1));
        u1.getRoles()
                .add(new UserRoles(u1, r2));
        u1.getRoles()
                .add(new UserRoles(u1, r3));

        u1.getUseremails()
                .add(new Useremail(u1,
                        "admin@email.local"));
        u1.getUseremails()
                .add(new Useremail(u1,
                        "admin@mymail.local"));

        u1.getUseremails().get(0).setUseremailid(20);
        u1.getUseremails().get(1).setUseremailid(21);
//        userService.save(u1);

        // data, user
        User u2 = new User("test cinnamon",
                "1234567",
                "cinnamon@lambdaschool.local");
        u2.setUserid(11);

        u2.getRoles()
                .add(new UserRoles(u2, r2));
        u2.getRoles()
                .add(new UserRoles(u2, r3));
        u2.getUseremails()
                .add(new Useremail(u2,
                        "cinnamon@mymail.local"));
        u2.getUseremails()
                .add(new Useremail(u2,
                        "hops@mymail.local"));
        u2.getUseremails()
                .add(new Useremail(u2,
                        "bunny@email.local"));
//        userService.save(u2);
        u2.getUseremails().get(0).setUseremailid(22);
        u2.getUseremails().get(1).setUseremailid(23);
        u2.getUseremails().get(2).setUseremailid(24);

        // user
        User u3 = new User("test barnbarn",
                "ILuvM4th!",
                "barnbarn@lambdaschool.local");
        u3.setUserid(12);

        u3.getRoles()
                .add(new UserRoles(u3, r2));
        u3.getUseremails()
                .add(new Useremail(u3,
                        "barnbarn@email.local"));
//        userService.save(u3);
        u3.getUseremails().get(0).setUseremailid(25);

        User u4 = new User("test puttat",
                "password",
                "puttat@school.lambda");
        u4.setUserid(13);

        u4.getRoles()
                .add(new UserRoles(u4, r2));
//        userService.save(u4);

        User u5 = new User("test misskitty",
                "password",
                "misskitty@school.lambda");
        u5.setUserid(14);

        u5.getRoles()
                .add(new UserRoles(u5, r2));
//        userService.save(u5);

        /************
         * Seed Books
         ************/

        Author a1 = new Author("John", "Mitchell");
        Author a2 = new Author("Dan", "Brown");
        Author a3 = new Author("Jerry", "Poe");
        Author a4 = new Author("Wells", "Teague");
        Author a5 = new Author("George", "Gallinger");
        Author a6 = new Author("Ian", "Stewart");

        a1.setAuthorid(1);
        a2.setAuthorid(2);
        a3.setAuthorid(3);
        a4.setAuthorid(4);
        a5.setAuthorid(5);
        a6.setAuthorid(6);

        Section s1 = new Section("Fiction");
        Section s2 = new Section("Technology");
        Section s3 = new Section("Travel");
        Section s4 = new Section("Business");
        Section s5 = new Section("Religion");

        s1.setSectionid(1);
        s2.setSectionid(2);
        s3.setSectionid(3);
        s4.setSectionid(4);
        s5.setSectionid(5);

        Book b1 = new Book("Test Flatterland", "9780738206752", 2001, s1);
        b1.getWrotes()
                .add(new Wrote(a6, new Book()));
        b1.setBookid(30);

        Book b2 = new Book("Test Digital Fortess", "9788489367012", 2007, s1);
        b2.getWrotes()
                .add(new Wrote(a2, new Book()));
        b2.setBookid(31);

        Book b3 = new Book("Test The Da Vinci Code", "9780307474278", 2009, s1);
        b3.getWrotes()
                .add(new Wrote(a2, new Book()));
        b3.setBookid(32);

        Book b4 = new Book("Test Essentials of Finance", "1314241651234", 0, s4);
        b4.getWrotes()
                .add(new Wrote(a3, new Book()));
        b4.getWrotes()
                .add(new Wrote(a5, new Book()));
        b4.setBookid(33);

        Book b5 = new Book("Test Calling Texas Home", "1885171382134", 2000, s3);
        b5.getWrotes()
                .add(new Wrote(a4, new Book()));
        b5.setBookid(34);

    }

    @After
    public void tearDown() throws
            Exception
    {
    }

    @Test
    public void listAllBooks() throws
            Exception
    {
        String apiUrl = "/books/books";

        Mockito.when(bookService.findAll()).thenReturn(bookList);

        RequestBuilder rb = MockMvcRequestBuilders.get(apiUrl)
                .accept(MediaType.APPLICATION_JSON);
        MvcResult r = mockMvc.perform(rb).andReturn();
        String tr = r.getResponse()
                .getContentAsString();

        ObjectMapper mapper = new ObjectMapper();
        String er = mapper.writeValueAsString(bookList);

        assertEquals("Rest API returns List", er, tr);
    }

    @Test
    public void getBookById() throws
            Exception
    {
        String apiUrl = "/books/book/21";
        Mockito.when(bookService.findBookById(21))
                .thenReturn(bookList.get(1));

        RequestBuilder rb = MockMvcRequestBuilders.get(apiUrl)
                .accept(MediaType.APPLICATION_JSON);
        MvcResult r = mockMvc.perform(rb)
                .andReturn();
        String tr = r.getResponse()
                .getContentAsString();

        ObjectMapper mapper = new ObjectMapper();
        String er = mapper.writeValueAsString(bookList.get(1));

        System.out.println(tr);
        assertEquals(er,
                tr);
    }

    @Test
    public void getNoBookById() throws
            Exception
    {
        String apiUrl = "/books/book/29991";
        Mockito.when(bookService.findBookById(29991))
                .thenReturn(null);

        RequestBuilder rb = MockMvcRequestBuilders.get(apiUrl)
                .accept(MediaType.APPLICATION_JSON);
        MvcResult r = mockMvc.perform(rb)
                .andReturn();
        String tr = r.getResponse()
                .getContentAsString();

        ObjectMapper mapper = new ObjectMapper();
        String er = "";

        System.out.println(tr);
        assertEquals(er,
                tr);
    }

    @Test
    public void addNewBook() throws
            Exception
    {
        String apiUrl = "/books/book";

        Section s9 = new Section("Fiction");
        //s9.setSectionid(1);
        Author a9 = new Author("Geronimo", "Stilton");
        //a9.setAuthorid(1);

        Book testBook = new Book("Test Red Pizzas", "1239287288289", 2008, s9);
        testBook.getWrotes().add(new Wrote(a9, new Book()));
        testBook.setBookid(1);

        ObjectMapper mapper = new ObjectMapper();
        String bookString = mapper.writeValueAsString(testBook);

        Mockito.when(bookService.save(any(Book.class))).thenReturn(testBook);

        RequestBuilder rb = MockMvcRequestBuilders.post(apiUrl).accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(bookString);

        mockMvc.perform(rb).andExpect(status().isCreated()).andDo(MockMvcResultHandlers.print());

    }

    @Test
    public void updateFullBook()
    {
    }

    @Test
    public void deleteBookById() throws
            Exception
    {
        String apiUrl = "/books/book/{bookid}";

        RequestBuilder rb = MockMvcRequestBuilders.delete(apiUrl,
                "21")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
        mockMvc.perform(rb)
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }
}