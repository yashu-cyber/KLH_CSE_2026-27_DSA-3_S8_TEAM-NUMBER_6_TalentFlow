# TalentFlow – Resume Search and Candidate Screening System

## About the Project

TalentFlow is a Resume Search and Candidate Screening System developed to simplify the process of searching, analyzing, and screening candidate resumes. The system addresses the challenges of manually reviewing large numbers of resumes by providing a structured platform for resume processing, candidate search, matching, and screening.

The project demonstrates the practical application of **Data Structures and Algorithms (DSA)** in recruitment-related tasks. It combines a React-based frontend with a Java Spring Boot backend connected through REST APIs. Apache PDFBox is used to extract text from supported PDF resumes.

TalentFlow aims to help recruiters organize candidate information, search for relevant skills and qualifications, compare candidate profiles, and support the initial screening process.

## Objectives

- Develop a system for uploading and processing candidate resumes.
- Extract relevant textual information from PDF resumes.
- Organize candidate information for efficient retrieval.
- Implement keyword-based resume searching.
- Apply string-matching algorithms to resume text processing.
- Support resume similarity analysis and candidate matching.
- Provide candidate optimization and ranking functionality.
- Integrate frontend and backend components using REST APIs.
- Demonstrate the practical application of DSA in a recruitment system.

## Key Features

### 1. Resume Upload and Processing
Allows recruiters to upload supported PDF resumes for processing and text extraction.

### 2. Resume Parsing
Uses Apache PDFBox to extract available text from supported PDF documents for subsequent processing.

### 3. Candidate Information Management
Organizes candidate information obtained from processed resumes for searching and review.

### 4. Candidate Search
Supports searching candidate information using relevant keywords and search criteria.

### 5. String Matching
Includes multiple string-matching techniques to demonstrate different approaches to locating patterns in resume text.

### 6. Resume Similarity
Provides functionality for comparing resume text or candidate information.

### 7. Candidate Matching and Ranking
Supports candidate matching and ranking to help recruiters organize and review relevant profiles.

### 8. Duplicate Analysis
Includes duplicate-related analysis as part of candidate information management.

### 9. Interview Scheduling
Includes interview scheduling in the recruitment workflow.

### 10. Full-Stack Integration
Connects a React frontend with a Java Spring Boot backend through REST APIs.

## Technology Stack

| Component | Technology |
|---|---|
| Frontend | React |
| Backend | Java |
| Backend Framework | Spring Boot |
| API Communication | REST APIs |
| PDF Text Extraction | Apache PDFBox |
| Algorithm Implementation | Data Structures and Algorithms in Java |

## Data Structures and Algorithms

TalentFlow incorporates the following algorithms and algorithmic components:

| Algorithm | Purpose |
|---|---|
| Aho–Corasick Algorithm | Searching for multiple patterns in text |
| Knuth–Morris–Pratt (KMP) | Efficient single-pattern string matching |
| Rabin–Karp Algorithm | Pattern matching using hash values |
| Z-Function Algorithm | String preprocessing and pattern matching |
| Edit Distance Algorithm | Measuring the edit operations needed to transform one string into another |
| Suffix Array | Organizing text suffixes to support search operations |
| Resume Similarity | Comparing resume text or candidate information |
| Bipartite Matching | Representing matching relationships between two sets |
| Candidate Optimization | Supporting candidate evaluation and organization |
| Scalable Resume Processing | Organizing resume-processing operations for larger collections |

These components demonstrate how different algorithmic techniques can be applied to text searching, comparison, matching, and candidate screening. Their exact behavior depends on the implementation in the source code.

## System Architecture

```text
                 Recruiter
                     |
                     v
              React Frontend
                     |
                     v
                 REST APIs
                     |
                     v
           Java Spring Boot Backend
                     |
          +----------+----------+
          |          |          |
          v          v          v
     Resume       Candidate    DSA
    Processing   Management  Algorithms
          |          |          |
          +----------+----------+
                     |
                     v
          Search / Matching /
          Similarity / Screening
                     |
                     v
             Results Display
                     |
                     v
              React Frontend
```

## System Workflow

1. The recruiter accesses the TalentFlow web interface.
2. A supported PDF resume is uploaded.
3. The backend processes the document and extracts available text.
4. Candidate information is prepared for subsequent operations.
5. The recruiter searches for candidates using relevant keywords or criteria.
6. Search, similarity, matching, and ranking functionality supports candidate review.
7. The recruiter reviews the results and proceeds with relevant recruitment activities, including interview scheduling where configured.

## Project Structure

The following is an illustrative structure. Update the folder names to match your actual GitHub repository.

```text
TalentFlow/
│
├── frontend/
│   └── React application
│
├── backend/
│   └── Java Spring Boot application
│
├── README.md
└── .gitignore
```

## Getting Started

### Prerequisites

Install the tools required by your project:

- Git
- Node.js and npm for the React frontend
- A compatible Java Development Kit (JDK)
- The backend build tool configured in the repository, such as Maven or Gradle
- Any additional services or database configured by the source code

### 1. Clone the Repository

```bash
git clone <YOUR_GITHUB_REPOSITORY_URL>
cd <YOUR_REPOSITORY_FOLDER>
```

Replace the placeholders with your actual GitHub repository URL and local folder name.

### 2. Run the Backend

Open a terminal in the backend directory.

For a Maven project that includes the Maven Wrapper, run:

**Windows:**

```bash
mvnw.cmd spring-boot:run
```

**Linux or macOS:**

```bash
./mvnw spring-boot:run
```

If the project uses a different build tool or does not include the Maven Wrapper, follow the instructions in the repository's build files.

Configure any required environment variables and services before starting the application.

### 3. Run the Frontend

Open another terminal in the frontend directory:

```bash
npm install
npm run dev
```

Open the local URL displayed by the frontend development server. If the available scripts differ, refer to the frontend's `package.json`.

## Testing

The following checks can be used to evaluate the application:

- Resume upload and file validation.
- PDF text extraction.
- Candidate search using relevant keywords.
- Testing the DSA algorithms with known inputs and outputs.
- Resume similarity and candidate matching.
- Candidate ranking and duplicate analysis.
- Frontend-to-backend REST API communication.
- Error handling for unsupported or invalid files.
- Interview scheduling, if enabled in the current implementation.

Actual test results and performance measurements should be added after executing the corresponding tests.

## Limitations

- PDF text extraction depends on the structure and contents of the uploaded document.
- Scanned PDFs may require OCR support if they contain images rather than extractable text.
- Resume formats and extracted information can vary.
- Keyword matching and similarity techniques may not capture every aspect of a candidate's qualifications.
- Candidate matching and ranking should support recruiter decisions rather than replace human evaluation.
- Performance and scalability depend on the implementation and execution environment.

## Future Enhancements

Potential future improvements include:

- OCR support for scanned resumes.
- Improved extraction of structured candidate information.
- Configurable job-skill matching.
- More comprehensive automated testing.
- Improved authentication and authorization.
- Enhanced accessibility and user experience.
- Performance evaluation using larger resume collections.

These are potential enhancements and should not be interpreted as features already implemented.

## Applications

TalentFlow can support recruitment-related activities in:

- Corporate recruitment.
- Human Resource departments.
- Recruitment agencies.
- Campus recruitment.
- Technical recruitment.
- Candidate database management.
- Initial resume screening.

## Team Members

- **KASULA YASHAS**
- **GNANESHWAR REDDY**
- **CHARAN**

## Project Information

- **Project Name:** TalentFlow
- **Project Title:** Resume Search and Candidate Screening System
- **Course:** Data Structures and Algorithms (DSA-3)
- **Academic Year:** 2026–2027

## Responsible Use

Use sample or authorized resumes for development and testing. Resume documents may contain personal information, so avoid committing private candidate data, credentials, or sensitive configuration files to a public GitHub repository.

Candidate matching and ranking are intended to assist recruiters and should not replace fair human review.

---

**Note:** Verify the repository structure, build commands, configuration requirements, and implemented features against your actual source code before publishing this README.
