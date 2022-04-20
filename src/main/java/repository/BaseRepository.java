package repository;

import response.BaseResponse;

import java.util.UUID;

public interface BaseRepository<T, R, RL> extends BaseResponse {

    T get(UUID id);

    RL getList();

    RL getList(UUID id);

    R add(T t);

    R editById(UUID id, T t);
}
