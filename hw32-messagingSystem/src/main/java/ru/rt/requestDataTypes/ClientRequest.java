package ru.rt.requestDataTypes;

import ru.rt.resultsDT.ClientsResult;

public class ClientRequest{

    public static ClientsResult getAllClients(){

        return new ClientsResult("FIND_ALL");
    }
}
