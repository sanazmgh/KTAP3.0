package view.messenger.listAndGroups;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import shared.form.GroupDataForm;
import shared.listener.EventListener;
import view.viewTools.ShowLists;

import java.net.URL;
import java.util.LinkedList;
import java.util.ResourceBundle;

public class MessengerListsFXMLController implements Initializable {

    @FXML
    private Pane newListPane;
    @FXML
    private Pane list1Pane;
    @FXML
    private Pane list2Pane;
    @FXML
    private Pane list3Pane;
    @FXML
    private Pane list4Pane;
    @FXML
    private Pane list5Pane;
    @FXML
    private Button backButton;
    @FXML
    private Button nextButton;

    private EventListener listener;
    private String type;
    private ShowLists showLists;
    private final NewListPane newList = new NewListPane();
    private LinkedList<ListViewPane> savedPanes = new LinkedList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        newListPane.getChildren().add(newList.getPane());

        LinkedList<Pane> mainPane = new LinkedList<>();
        mainPane.add(list1Pane);
        mainPane.add(list2Pane);
        mainPane.add(list3Pane);
        mainPane.add(list4Pane);
        mainPane.add(list5Pane);

        showLists = new ShowLists();
        showLists.setMainPanes(mainPane);
        showLists.setButtons(backButton, nextButton);
    }

    public void setListener(EventListener listener) {
        this.listener = listener;
        newList.getLoader().setListener(listener);
    }

    public void setType(String type)
    {
        this.type = type;
        newList.getLoader().setType(type);
    }

    public void setData(LinkedList<GroupDataForm> forms)
    {
        if(savedPanes.size() != forms.size())
            savedPanes = new LinkedList<>();

        LinkedList<Pane> showPanes = new LinkedList<>();

        int savedSize = savedPanes.size();

        for(int i=0 ; i<forms.size() ; i++)
        {
            GroupDataForm form = forms.get(i);

            if(type.equals("Lists") || (type.equals("Groups") && form.getUsernames().size() > 2))
            {
                if(savedSize != forms.size())
                {
                    ListViewPane listViewPane = new ListViewPane();
                    listViewPane.getLoader().setListListener(listener);
                    listViewPane.getLoader().setType(type);
                    listViewPane.getLoader().setData(form);
                    showPanes.add(listViewPane.getPane());
                    savedPanes.add(listViewPane);
                }

                else
                {
                    System.out.println(forms.size() + " " + savedPanes.size());
                    savedPanes.get(i).getLoader().setData(form);
                    showPanes.add(savedPanes.get(i).getPane());
                }
            }
        }

        showLists.setShowPanes(showPanes);
        showLists.setPanes();

    }

    public void backPage()
    {
        showLists.prevPage();
    }

    public void nextPage()
    {
        showLists.nextPage();
    }

}
