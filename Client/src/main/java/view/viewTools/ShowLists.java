package view.viewTools;

import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

import java.util.LinkedList;

public class ShowLists {
    private int page = 1;
    private LinkedList<Pane> mainPanes;
    private LinkedList<Pane> showPanes;
    private Button nextButton;
    private Button backButton;

    public void setMainPanes(LinkedList<Pane> mainPanes) {
        this.mainPanes = mainPanes;
    }

    public void setShowPanes(LinkedList<Pane> showPanes) {
        this.showPanes = showPanes;
    }

    public void setButtons(Button backButton, Button nextButton)
    {
        this.backButton = backButton;
        this.nextButton = nextButton;
    }

    public void setPanes()
    {
        int totalPanes = showPanes.size();
        int itemsEachPage = mainPanes.size();
        //page = Math.min(page, (int)Math.ceil((double)showPanes.size()/(double)mainPanes.size()));
        int ind = (page-1) * itemsEachPage;

        Platform.runLater(
                () ->
                {
                    for (int i = 0; i < itemsEachPage; i++) {
                        mainPanes.get(i).getChildren().clear();

                        if (ind + i < totalPanes)
                            mainPanes.get(i).getChildren().add(showPanes.get(ind + i));
                    }
                }
        );

        backButton.setDisable(false);
        nextButton.setDisable(false);

        if(page == 1)
            backButton.setDisable(true);

        if(page >= Math.ceil((double)showPanes.size()/(double)mainPanes.size()))
            nextButton.setDisable(true);
    }

    public void nextPage()
    {
        page++;
        setPanes();
    }

    public void prevPage()
    {
        page--;
        setPanes();
    }
}
