package vehicle;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;



@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public class VehicleRestControllerTest {

    @Rule
    public  final JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("build/generated-snippets");

    private MockMvc mockMvc;

    private Vehicle vehicle;

    private List<Recalls> recalls = new ArrayList<>();

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private RecallsRepository recallsRepository;

    @Autowired
    private WebApplicationContext context;

    private HttpMessageConverter mappingJackson2HttpMessageConverter;

    private String vin = "123abc";

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));
    FieldDescriptor[] recall = new FieldDescriptor[] {
            fieldWithPath("id").description("id of recall record"),
            fieldWithPath("description").description("recall info")
    };

    @Autowired
    void setConverters(HttpMessageConverter<?>[] converters) {

        this.mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream()
                .filter(hmc -> hmc instanceof MappingJackson2HttpMessageConverter)
                .findAny()
                .orElse(null);

        assertNotNull("the JSON message converter must not be null",
                this.mappingJackson2HttpMessageConverter);
    }


    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
                .apply(documentationConfiguration(this.restDocumentation))
                .alwaysDo(document("{method-name}/{step}/"))
                .build();

        this.recallsRepository.deleteAllInBatch();
        this.vehicleRepository.deleteAllInBatch();

        vehicle = vehicleRepository.save(new Vehicle(vin));
        recalls.add(recallsRepository.save(new Recalls(vehicle, "test recall 1 on " + vehicle.getVin())));
        recalls.add(recallsRepository.save(new Recalls(vehicle, "test recall 2 on " + vehicle.getVin())));
    }


    @Test
    public void recallsForVin() throws Exception {
        this.mockMvc.perform(get("/recall/{vin}", vin))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(recalls.get(0).getId().intValue())))
                .andExpect(jsonPath("$[0].description", is(recalls.get(0).getDescription())))
                .andExpect(jsonPath("$[1].id", is(recalls.get(1).getId().intValue())))
                .andExpect(jsonPath("$[1].description", is(recalls.get(1).getDescription())))
                .andDo(document("recallsForVin", preprocessResponse(prettyPrint())));
    }

}
