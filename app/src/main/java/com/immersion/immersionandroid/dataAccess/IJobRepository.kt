package com.immersion.immersionandroid.dataAccess

import com.immersion.immersionandroid.domain.Job

// TODO: standarize names
interface IJobRepository {
    suspend fun createJob(job: Job): Boolean;
}