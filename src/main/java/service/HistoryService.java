package service;

import jsonFile.CollectionsTypeFactory;
import jsonFile.FileUrls;
import jsonFile.FileUtils;
import jsonFile.Json;
import lombok.SneakyThrows;
import model.History;
import repository.HistoryRepository;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class HistoryService extends HistoryRepository {

    @Override
    public History get(UUID id) {
        return null;
    }

    @Override
    public List<History> getList() {
        return getHistoryListFromFile();
    }


    @Override
    public List<History> getList(UUID sellerOrUserId) {
        return null;
    }

    @Override
    public String add(History history) {
        List<History> historyList = getHistoryListFromFile();
        historyList.add(history);

        setHistoryListToFile(historyList);
        return SUCCESS;
    }

    @Override
    public String editById(UUID id, History history) {
        return null;
    }


    public GetHistories getUserHistories = userId -> {
        StringBuilder sb = new StringBuilder();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        for (History history : getHistoryListFromFile()) {
            if (history.getUserId().equals(userId)) {
                String s = "Seller: " + history.getSellerName() +
                        "\t|\tProduct name: " + history.getProductName() +
                        "\t|\tProduct amount: " + history.getAmount() +
                        "\t|\tBought day: " + df.format(history.getCreatedDate()) + "\n\n";
                sb.append(s);
            }
        }
        return sb;
    };


    public List<History> getHistoryListFromFile() {
        String historyJsonStringFromFile = FileUtils.readFromFile(FileUrls.historyUrl);
        List<History> historyList;
        try {
            historyList = Json.objectMapper.readValue(historyJsonStringFromFile, CollectionsTypeFactory.listOf(History.class));
        } catch (Exception e) {
            System.out.println(e);
            historyList = new ArrayList<>();
        }
        return historyList;
    }

    @SneakyThrows
    public void setHistoryListToFile(List<History> historyList) {
        String newHistoryJsonFromObject = Json.prettyPrint(historyList);
        FileUtils.writeToFile(FileUrls.historyUrl, newHistoryJsonFromObject);
    }
}
