/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package addressbook;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import addressbook.listeners.IActionListener;
import addressbook.listeners.IShowDataListener;
import addressbook.listeners.ISortActionListener;

/**
 *
 * @author Igor Gayvan
 */
public class Console {

    private Scanner scanner;

    private List<IActionListener> actionListeners;
    private List<IShowDataListener> showDataListeners;
    private List<ISortActionListener> sortActionListeners;

    private ConsoleModeWorking modeWorking;

    private String inputText;

    public Console(InputStream inputStream) {
        this.scanner = new Scanner(inputStream);

        this.actionListeners = new ArrayList<>();
        this.showDataListeners = new ArrayList<>();
        this.sortActionListeners = new ArrayList<>();

        this.modeWorking = ConsoleModeWorking.CHOICE_MODE;
    }

    public void addActionListener(IActionListener actionListener) {
        actionListeners.add(actionListener);
    }

    public void addSortActionListener(ISortActionListener sortActionListener) {
        sortActionListeners.add(sortActionListener);
    }

    public void addShowDataListener(IShowDataListener showDataListener) {
        showDataListeners.add(showDataListener);
    }

    public String getInputText() {
        return inputText.trim();
    }

    public void setInputText(String inputText) {
        this.inputText = inputText;
    }

    public ConsoleModeWorking getModeWorking() {
        return modeWorking;
    }

    public void setModeWorking(ConsoleModeWorking modeWorking) {
        this.modeWorking = modeWorking;
    }

    public void working() {
        while (true) {
            if (ConsoleModeWorking.CHOICE_MODE.equals(modeWorking)) {
                System.out.println("1 - show list of contacts");
                System.out.println("2 - add new contact");
                System.out.println("3 - show information about contact");
                System.out.println("5 - refresh");
                System.out.println("7 - sort by phone");
                System.out.println("8 - sort by any field");
                System.out.println("9 - filter by full name");
                System.out.println("10 - delete contact");
                System.out.println("11 - update contact");
                System.out.println("0 - exit");
                System.out.print("Your choice? ");
            }

            inputText = scanner.nextLine().trim();

            switch (modeWorking) {
                case FILTER_BY_FULL_NAME: {
                    for (IShowDataListener addressBookAction : showDataListeners) {
                        addressBookAction.filterContactsByFullNameAction();
                    }
                    break;
                }
                case SHOW_CONTACT: {
                    for (IShowDataListener addressBookAction : showDataListeners) {
                        addressBookAction.showContactAction();
                    }
                    break;
                }
                case ADD_CONTACT: {
                    for (IActionListener addressBookAction : actionListeners) {
                        addressBookAction.addContactAction();
                    }
                    break;
                }
                case DEL_CONTACT: {
                    for (IActionListener addressBookAction : actionListeners) {
                        addressBookAction.delContactAction();
                    }
                    break;
                }
                case UPD_CONTACT: {
                for (IActionListener addressBookAction : actionListeners) {
                    addressBookAction.updContactAction();
                }
                    break;
                }
                case SORT_BY_ANY_FIELD: {
                    for (ISortActionListener addressBookAction : sortActionListeners) {
                        addressBookAction.sortByAnyField();
                    }
                    break;
                }
                case CHOICE_MODE:
                    switch (inputText.toLowerCase()) {
                        case "0":
                            for (IActionListener addressBookAction : actionListeners) {
                                addressBookAction.exitAction();
                            }
                            break;
                        case "1":
                            for (IShowDataListener addressBookAction : showDataListeners) {
                                addressBookAction.showListContactsAction();
                            }
                            break;
                        case "2":
                            setModeWorking(ConsoleModeWorking.ADD_CONTACT);
                            for (IShowDataListener addressBookAction : showDataListeners) {
                                addressBookAction.showPromptInputContactAction();
                            }
                            break;
                        case "10":
                            setModeWorking(ConsoleModeWorking.DEL_CONTACT);
                            for (IShowDataListener addressBookAction : showDataListeners) {
                                addressBookAction.showPromptInputContactIdAction();
                            }
                            break;
                        case "11":
                            setModeWorking(ConsoleModeWorking.UPD_CONTACT);
                            for (IShowDataListener addressBookAction : showDataListeners) {
                                addressBookAction.showPromptInputContactIdAction();
                            }
                            break;
                        case "3":
                            setModeWorking(ConsoleModeWorking.SHOW_CONTACT);
                            for (IShowDataListener addressBookAction : showDataListeners) {
                                addressBookAction.showPromptInputContactIdAction();
                            }
                            break;
                        case "5":
                            setModeWorking(ConsoleModeWorking.REFRESH);
                            for (IActionListener addressBookAction : actionListeners) {
                                addressBookAction.refreshDataAction();
                            }
                            break;
                        case "7":
                            setModeWorking(ConsoleModeWorking.SORT_BY_PHONE);
                            for (ISortActionListener addressBookAction : sortActionListeners) {
                                addressBookAction.sortByPhoneAction();
                            }
                            break;
                        case "8":
                            setModeWorking(ConsoleModeWorking.SORT_BY_ANY_FIELD);
                            for (ISortActionListener addressBookAction : sortActionListeners) {
                                addressBookAction.sortByAnyFieldAction();
                            }
                            break;
                        case "9":
                            setModeWorking(ConsoleModeWorking.FILTER_BY_FULL_NAME);
                            for (IShowDataListener addressBookAction : showDataListeners) {
                                addressBookAction.showPromptInputFilterNameFullAction();
                            }
                            break;

                        default:
                            System.out.println("Make your choice");
                    }
            }
        }
    }

}
