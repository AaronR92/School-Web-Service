# Student grade web service

## Requests and authorization

|                                 | Administrator | Teacher | Student | Anonymous |
|:--------------------------------|:--------------|:--------|:--------|:----------|
| `POST /api/user/add`            | +             | -       | -       | -         |
| `PUT /api/user/change/password` | -             | +       | +       | -         |
| `GET /api/user/all`             | +             | -       | -       | -         |
| `DELETE /api/user`              | +             | -       | -       | -         |
| `POST /api/student/mark/add`    | -             | +       | -       | -         |
| `DELETE /api/student/mark/add`  | -             | +       | -       | -         |
| `POST /api/subject/new`         | +             | -       | -       | -         |
| `DELETE /api/subject/new`       | +             | -       | -       | -         |