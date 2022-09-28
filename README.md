# Student Grade Web Service

## Requests and authorization

|                                   | Administrator | Teacher | Student | Anonymous |
|:----------------------------------|:--------------|:--------|:--------|:----------|
| `GET /api/group`                  | +             | +       | -       | -         |
| `GET /api/group/all`              | +             | +       | -       | -         |
| `POST /api/group`                 | +             | -       | -       | -         |
| `DELETE /api/group`               | +             | -       | -       | -         |
| `POST /api/user/signup`           | -             | -       | -       | +         |
| `PUT /api/user/change/password`   | -             | +       | +       | -         |
| `PUT /api/user/change/role`       | +             | -       | -       | -         |
| `PUT /api/user/change/username`   | +             | -       | -       | -         |
| `GET /api/user/find/all`          | +             | -       | -       | -         |
| `GET /api/user/find`              | +             | +       | +       | -         |
| `DELETE /api/user`                | +             | -       | -       | -         |
| `POST /api/student/mark/add`      | -             | +       | -       | -         |
| `DELETE /api/student/mark/delete` | -             | +       | -       | -         |
| `POST /api/subject`               | +             | -       | -       | -         |
| `DELETE /api/subject`             | +             | -       | -       | -         |